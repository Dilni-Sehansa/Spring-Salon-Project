package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.FeedbackDTO;

import java.util.List;

public interface FeedbackService {

    FeedbackDTO saveFeedback(FeedbackDTO feedbackDTO);
    List<FeedbackDTO> getAllFeedbacks();
    List<FeedbackDTO> getFeedbacksByCustomerId(Long customerId);
    List<FeedbackDTO> getFeedbacksByCustomerName(String name);
    List<FeedbackDTO> getFeedbacksByRating(Integer rating);
//    List<FeedbackDTO> findByCustomer_CustomerNameContainingIgnoreCase(String name);


}
