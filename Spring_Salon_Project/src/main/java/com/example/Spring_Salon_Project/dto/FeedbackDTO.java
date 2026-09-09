package com.example.Spring_Salon_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTO {
    private Long feedbackId;
    private Long customerId;
    private String customerName;
    private Integer rating;
    private String comments;
    private LocalDate feedbackDate;

}
