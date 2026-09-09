package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.StaffDTO;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import com.example.Spring_Salon_Project.enumiration.StaffStatus;


import java.util.List;

public interface StaffService {
    StaffDTO saveStaff(StaffDTO staffDTO);

    void updateStaff(StaffDTO staffDTO);

    void deleteStaff(Long staffId);

    StaffDTO getStaffByUserId(long userId);

    List<StaffDTO> getAllStaff();

    List<StaffDTO> getStaffByStatus(StaffStatus staffStatus);

    List<StaffDTO> filterStaff(String specialization);

    StaffDTO selectStaff(long staffId);

    void updateStaffStatus(Long staffId, StaffStatus status);

}
