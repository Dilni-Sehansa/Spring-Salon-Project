package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffServiceDTO {
    private Long staffServiceId;
    private Long staffId;
    private String staffName;
    private Long serviceId;
    private String serviceName;
    private Status staffServiceStatus;


}
