package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AiStyleDnaResponseDTO;
import com.example.Spring_Salon_Project.entity.Product;
import com.example.Spring_Salon_Project.entity.SaloonService;
import com.example.Spring_Salon_Project.repository.ProductRepository;
import com.example.Spring_Salon_Project.repository.SaloonServiceRepository;
import com.example.Spring_Salon_Project.service.AiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SaloonServiceRepository saloonServiceRepository;
    private final ProductRepository productRepository;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.model}")
    private String model;

    @Override
    public AiStyleDnaResponseDTO analyzeStyleDna(MultipartFile image, Long customerId, String preferredStyle) {
        try {
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("Please upload a clear face photo");
            }

            log.info("Received image: name={}, size={} bytes, type={}",
                    image.getOriginalFilename(), image.getSize(), image.getContentType());

            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

            List<SaloonService> allServices = new ArrayList<>();
            saloonServiceRepository.findAll().forEach(s -> {
                if (s.getServiceStatus() != null &&
                        s.getServiceStatus().name().equalsIgnoreCase("ACTIVE")) {
                    allServices.add(s);
                }
            });

            List<Product> allProducts = new ArrayList<>();
            productRepository.findAll().forEach(p -> {
                if (p.getProductStatus() != null &&
                        p.getProductStatus().name().equalsIgnoreCase("ACTIVE")) {
                    allProducts.add(p);
                }
            });

            String servicesText = allServices.stream()
                    .map(s -> s.getServiceId() + "|" + s.getServiceName() + "|" + s.getPrice())
                    .collect(Collectors.joining("\n"));

            String productsText = allProducts.stream()
                    .map(p -> p.getProductId() + "|" + p.getProductName() + "|" + p.getPrice())
                    .collect(Collectors.joining("\n"));

            String preference = (preferredStyle == null || preferredStyle.isBlank())
                    ? "balanced modern look" : preferredStyle;

            String systemPrompt = """
    You are a professional human face analysis AI for a beauty salon.
    
    STRICT RULES - NEVER BREAK THESE:
    
    1. ONLY analyze if there is a clear HUMAN face in the photo.
    
    2. If the photo contains:
       - Animals (cat, dog, bird, etc.)
       - Objects
       - Cartoon / drawing
       - Blurry face
       - No face at all
       - Multiple people (group photo)
       - Only body / back of head
       
       THEN you MUST return EXACTLY this JSON:
       {
         "faceShape": "No clear face detected",
         "skinUndertone": "Unable to determine",
         "hairType": "Unable to determine",
         "overallLookSummary": "No clear human face was detected. Please upload a clear front-facing photo of one person.",
         "recommendedHairStyles": [],
         "recommendedHairColors": [],
         "recommendedServiceIds": [],
         "recommendedProductIds": [],
         "packageSuggestion": "Please upload a clear human face photo",
         "confidenceNote": "Low - Not a human face"
       }
    
    3. ONLY if a clear single HUMAN face is present:
       - Analyze face shape, skin undertone and hair type normally.
       - Never analyze animals or non-human subjects.
    
    Return ONLY valid JSON. No extra text.
    """;

            String userText = """
    Available Services (id|name|price):
    %s
    
    Available Products (id|name|price):
    %s
    
    Customer preferred style: %s
    
    IMPORTANT:
    - First check if the photo has a clear HUMAN face.
    - If it is an animal, object, or no face → return "No clear face detected".
    - Only analyze real human faces.
    """.formatted(servicesText, productsText, preference);

            Map<String, Object> imageUrl = new HashMap<>();
            imageUrl.put("url", "data:" + mimeType + ";base64," + base64Image);

            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image_url");
            imageContent.put("image_url", imageUrl);

            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", userText);

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", List.of(textContent, imageContent));

            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(systemMessage, userMessage));
            requestBody.put("temperature", 0.2);          // low temperature for accuracy
            requestBody.put("max_tokens", 1500);
            requestBody.put("response_format", Map.of("type", "json_object"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class);

            log.info("Raw AI response: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").path(0).path("message").path("content").asText();

            if (content == null || content.isBlank()) {
                throw new RuntimeException("AI returned empty content");
            }

            JsonNode aiJson = objectMapper.readTree(content);

            List<AiStyleDnaResponseDTO.RecommendedService> recServices = new ArrayList<>();
            if (aiJson.has("recommendedServiceIds") && aiJson.get("recommendedServiceIds").isArray()) {
                for (JsonNode idNode : aiJson.get("recommendedServiceIds")) {
                    Long id = idNode.asLong();
                    allServices.stream()
                            .filter(s -> s.getServiceId().equals(id))
                            .findFirst()
                            .ifPresent(s -> recServices.add(
                                    AiStyleDnaResponseDTO.RecommendedService.builder()
                                            .serviceId(s.getServiceId())
                                            .serviceName(s.getServiceName())
                                            .price(s.getPrice())
                                            .reason("Matches your face shape & skin tone")
                                            .build()
                            ));
                }
            }

            List<AiStyleDnaResponseDTO.RecommendedProduct> recProducts = new ArrayList<>();
            if (aiJson.has("recommendedProductIds") && aiJson.get("recommendedProductIds").isArray()) {
                for (JsonNode idNode : aiJson.get("recommendedProductIds")) {
                    Long id = idNode.asLong();
                    allProducts.stream()
                            .filter(p -> p.getProductId().equals(id))
                            .findFirst()
                            .ifPresent(p -> recProducts.add(
                                    AiStyleDnaResponseDTO.RecommendedProduct.builder()
                                            .productId(p.getProductId())
                                            .productName(p.getProductName())
                                            .price(p.getPrice())
                                            .reason("Complements the recommended look")
                                            .build()
                            ));
                }
            }

            double total = recServices.stream().mapToDouble(AiStyleDnaResponseDTO.RecommendedService::getPrice).sum()
                    + recProducts.stream().mapToDouble(AiStyleDnaResponseDTO.RecommendedProduct::getPrice).sum();

            return AiStyleDnaResponseDTO.builder()
                    .faceShape(aiJson.path("faceShape").asText("Unknown"))
                    .skinUndertone(aiJson.path("skinUndertone").asText("Unknown"))
                    .hairType(aiJson.path("hairType").asText("Unknown"))
                    .overallLookSummary(aiJson.path("overallLookSummary").asText(""))
                    .recommendedHairStyles(jsonArrayToList(aiJson.path("recommendedHairStyles")))
                    .recommendedHairColors(jsonArrayToList(aiJson.path("recommendedHairColors")))
                    .recommendedServices(recServices)
                    .recommendedProducts(recProducts)
                    .packageSuggestion(aiJson.path("packageSuggestion").asText(""))
                    .estimatedPackagePrice(total)
                    .confidenceNote(aiJson.path("confidenceNote").asText("Medium"))
                    .build();

        } catch (Exception e) {
            log.error("StyleDNA analysis failed", e);
            throw new RuntimeException("AI analysis failed: " + e.getMessage());
        }
    }

    private List<String> jsonArrayToList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node != null && node.isArray()) {
            node.forEach(n -> list.add(n.asText()));
        }
        return list;
    }
}