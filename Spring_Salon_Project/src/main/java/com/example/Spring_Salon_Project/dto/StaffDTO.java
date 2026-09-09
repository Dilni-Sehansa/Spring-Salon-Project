package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffDTO {
    private Long staffId;
    private String specialization;
    private String bio;
    private Integer experienceYears;
    private Long userId;
    private String userName;
    private StaffStatus staffStatus;
}
