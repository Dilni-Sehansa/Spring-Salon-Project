package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AiChatResponse;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ai")
@RequiredArgsConstructor
@CrossOrigin
public class AiController {

    private final AiService aiService;

//    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
//    public CommonResponse chat(@RequestBody AiChatRequest request) {
//        String reply = aiService.getChatReply(request.getMessage());
//        return new CommonResponse(0, new AiChatResponse(reply), "Success");
//    }

    @GetMapping(value = "/recommend-services", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse recommendServices(
            @RequestParam(value = "customerId", required = false) Long customerId) {

        String recommendations = aiService.getServiceRecommendations(customerId);
        return new CommonResponse(0, new AiChatResponse(recommendations), "Recommendations generated successfully");
    }
}