package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;
import com.example.Spring_Salon_Project.dto.AppointmentDetailDTO;
import com.example.Spring_Salon_Project.entity.SaloonService;
import com.example.Spring_Salon_Project.repository.AppointmentDetailRepository;
import com.example.Spring_Salon_Project.repository.AppointmentRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SaloonServiceRepository saloonServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.model}")
    private String model;

//    @Override
//    public String getChatReply(String userMessage) {
//        try {
//            String systemPrompt = """
//                You are a helpful AI assistant for "Glow Salon" - a beauty salon management system.
//
//                You can help customers with:
//                - Information about services (Hair cut, Facial, Manicure, Pedicure, Hair coloring, etc.)
//                - Approximate prices (you can say average prices if exact ones are not known)
//                - Available appointment times (suggest morning, afternoon, evening)
//                - General beauty tips
//                - How to book an appointment
//
//                Rules:
//                - Always be polite, friendly and professional
//                - Reply in the same language the user is using (Sinhala or English)
//                - If you don't know exact price or availability, politely say to contact the salon or check the website
//                - Keep answers short and clear
//                - Do not make up fake bookings
//                """;
//
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("model", model);
//            requestBody.put("messages", List.of(
//                    Map.of("role", "system", "content", systemPrompt),
//                    Map.of("role", "user", "content", userMessage)
//            ));
//            requestBody.put("temperature", 0.7);
//            requestBody.put("max_tokens", 500);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(apiKey);
//
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    apiUrl,
//                    HttpMethod.POST,
//                    entity,
//                    String.class
//            );
//
//            JsonNode root = objectMapper.readTree(response.getBody());
//            String reply = root.path("choices").path(0).path("message").path("content").asText();
//
//            return reply != null && !reply.isEmpty() ? reply : "Sorry, I couldn't generate a reply right now.";
//
//        } catch (Exception e) {
//            log.error("AI Chat error: {}", e.getMessage());
//            return "Sorry, something went wrong. Please try again later.";
//        }
//    }

    @Override
    public String getServiceRecommendations(Long customerId) {
        try {
            List<SaloonService> allServices = saloonServiceRepository.findAll();
            String availableServices = allServices.stream()
                    .filter(s -> s.getServiceStatus() == null ||
                            "ACTIVE".equalsIgnoreCase(s.getServiceStatus().name()))
                    .map(s -> s.getServiceName() + " (LKR " + s.getPrice() + ")")
                    .collect(Collectors.joining(", "));

            String pastServices = "No previous services";
            if (customerId != null) {
                List<AppointmentDTO> appointments =
                        appointmentRepository.getAppointmentsByCustomerId(customerId);

                if (appointments != null && !appointments.isEmpty()) {
                    StringBuilder past = new StringBuilder();
                    for (AppointmentDTO appt : appointments) {
                        List<AppointmentDetailDTO> details =
                                appointmentDetailRepository.getDetailsByAppointmentId(appt.getAppointmentId());
                        if (details != null) {
                            for (AppointmentDetailDTO d : details) {
                                if (d.getServiceName() != null) {
                                    past.append(d.getServiceName()).append(", ");
                                }
                            }
                        }
                    }
                    if (past.length() > 0) {
                        pastServices = past.toString();
                    }
                }
            }

            String prompt = """
               You are a professional beauty salon consultant for "Glow Salon".
               Available services: %s
               Customer's previous services: %s
                Recommend the BEST 3 to 5 services for this customer.
                Format EXACTLY like this (no extra text):
                • Service Name – short reason why it is suitable
                • Service Name – short reason why it is suitable
                • Service Name – short reason why it is suitable
                 
Keep it short, friendly and clear. Do not use markdown ** or numbers.
                  """.formatted(availableServices, pastServices);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", "You are a helpful salon service recommendation assistant."),
                    Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("temperature", 0.6);
            requestBody.put("max_tokens", 600);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String reply = root.path("choices").path(0).path("message").path("content").asText();

            return (reply != null && !reply.isEmpty())
                    ? reply
                    : "Sorry, could not generate recommendations right now.";

        } catch (Exception e) {
            log.error("AI Recommendation FULL error: ", e);

            String msg = e.getMessage() != null ? e.getMessage() : "Unknown error";

            if (msg.contains("model_not_found") || msg.contains("does not exist")) {
                return "AI model not available. Please change ai.model in application.properties (try llama-3.3-70b-versatile).";
            }

            return "Error: " + msg;
        }
    }
}