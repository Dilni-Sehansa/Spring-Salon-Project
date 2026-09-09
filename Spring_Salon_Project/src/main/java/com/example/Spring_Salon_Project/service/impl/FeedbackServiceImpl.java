package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.FeedbackDTO;
import com.example.Spring_Salon_Project.entity.Customer;
import com.example.Spring_Salon_Project.entity.Feedback;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.CustomerRepository;
import com.example.Spring_Salon_Project.repository.FeedbackRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    @Override
    public FeedbackDTO saveFeedback(FeedbackDTO feedbackDTO) {
        log.info("Executing saveFeedback method");

        try {
            if (feedbackDTO.getCustomerId() == null) {
                throw new CustomerException(400, "Customer ID is required!");
            }
            Optional<Customer> optionalCustomer = customerRepository.findById(feedbackDTO.getCustomerId());
            if (optionalCustomer.isEmpty()) {
                throw new CustomerException(404, "Customer not found for ID: " + feedbackDTO.getCustomerId());
            }

            if (feedbackDTO.getRating() == null || feedbackDTO.getRating() < 1 || feedbackDTO.getRating() > 5) {
                throw new CustomerException(400, "Rating must be between 1 and 5!");
            }
            Customer customer = optionalCustomer.get();
            Feedback feedback = new Feedback();
            feedback.setCustomer(customer);
            feedback.setRating(feedbackDTO.getRating());
            feedback.setComments(feedbackDTO.getComments());

            Feedback savedFeedback = feedbackRepository.save(feedback);
            log.info("Feedback saved successfully with ID: {}", savedFeedback.getFeedbackId());

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("FEEDBACK");
            logDTO.setEntityId(savedFeedback.getFeedbackId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("New feedback created: " + savedFeedback.getFeedbackId());
            auditLogService.saveAuditLog(logDTO);

            return new FeedbackDTO(
                    savedFeedback.getFeedbackId(),
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    savedFeedback.getRating(),
                    savedFeedback.getComments(),
                    savedFeedback.getFeedbackDate()
            );


        } catch (CustomerException e) {
            log.error("Validation error in saveFeedback: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in saving feedback: {}", e.getMessage());
            throw new CustomerException(500, "Error in saving Feedback");
        }
    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.getAllFeedbacks();
    }

    @Override
    public List<FeedbackDTO> getFeedbacksByCustomerId(Long customerId) {
        log.info("Execute method getFeedbacksByCustomerId for customerId: {}", customerId);
        return feedbackRepository.findByCustomer_CustomerId(customerId);
    }

    @Override
    public List<FeedbackDTO> getFeedbacksByCustomerName(String name) {
        log.info("Execute method getFeedbacksByCustomerName for customerName: {}", name);
        return feedbackRepository.findByCustomer_CustomerName(name);
    }

    @Override
    public List<FeedbackDTO> getFeedbacksByRating(Integer rating) {
        log.info("Executing getFeedbacksByRating for rating: {}", rating);

        if (rating == null || rating < 1 || rating > 5) {
            throw new CustomerException(400, "Invalid rating! Rating must be between 1 and 5.");
        }

        List<Feedback> feedbackList = feedbackRepository.findByRating(rating);
        List<FeedbackDTO> dtoList = new ArrayList<>();

        if (!feedbackList.isEmpty()) {
            for (Feedback f : feedbackList) {
                FeedbackDTO dto = new FeedbackDTO(
                        f.getFeedbackId(),
                        f.getCustomer().getCustomerId(),
                        f.getCustomer().getCustomerName(),
                        f.getRating(),
                        f.getComments(),
                        f.getFeedbackDate()
                );
                dtoList.add(dto);
            }
        }

        return dtoList;
    }
//    @Override
//    public List<FeedbackDTO> findByCustomer_CustomerNameContainingIgnoreCase(String name) {
//        return List.of();
//    }
}
/*
*  private Long feedbackId;
    private String customerName;
    private Integer rating;
    private String comments;
    private LocalDate feedbackDate;*/
