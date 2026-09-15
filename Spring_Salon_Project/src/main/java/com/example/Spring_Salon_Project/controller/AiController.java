package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AiStyleDnaResponseDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin
public class AiController {

    private final AiService aiService;

    @PostMapping("/style-dna")
    public ResponseEntity<CommonResponse> analyzeStyleDna(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "preferredStyle", required = false) String preferredStyle) {

        AiStyleDnaResponseDTO result = aiService.analyzeStyleDna(image, customerId, preferredStyle);

        CommonResponse response = new CommonResponse();
        response.setStatus(200);
        response.setMessage("StyleDNA analysis completed successfully");
        response.setBody(result);

        return ResponseEntity.ok(response);
    }
}