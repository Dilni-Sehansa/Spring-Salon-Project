package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.FeedbackDTO;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/feedback-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveFeedback(@RequestBody FeedbackDTO feedbackDTO){
        FeedbackDTO saveFeedback = feedbackService.saveFeedback(feedbackDTO);
        return new CommonResponse(0,saveFeedback,"Feedback Saved Successfully");
    }

    @GetMapping(value = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllFeedback(){
        List<FeedbackDTO> feedbackDTOS = feedbackService.getAllFeedbacks();
        return new CommonResponse(0,feedbackDTOS,"Feedback Loaded Successfully");
    }

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFeedbackByCustomerId(@PathVariable long customerId) {
        List<FeedbackDTO> feedbackDTOS = feedbackService.getFeedbacksByCustomerId(customerId);
        return new CommonResponse(0, feedbackDTOS, "Feedback Loaded Successfully");
    }

    @GetMapping(value = "/customer/name/{customerName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFeedbackByCustomerName(@PathVariable String customerName) {
        List<FeedbackDTO> feedbackDTOS = feedbackService.getFeedbacksByCustomerName(customerName);
        return new CommonResponse(0, feedbackDTOS, "Feedback Loaded Successfully");
    }

    @GetMapping(value = "/by-rating/{rating}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFeedbackByRating(@PathVariable Integer rating) {
        List<FeedbackDTO> feedbackDTOS = feedbackService.getFeedbacksByRating(rating);
        return new CommonResponse(0, feedbackDTOS, "Feedback Loaded Successfully");
    }
}
