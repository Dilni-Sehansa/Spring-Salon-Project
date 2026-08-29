package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.StaffServiceDTO;
import com.example.Spring_Salon_Project.enumiration.Status;

import java.util.List;

public interface StaffServiceService {

    StaffServiceDTO saveStaffService(StaffServiceDTO staffServiceDTO);

    List<StaffServiceDTO> saveMultipleStaffServices(Long staffId, List<Long> serviceIds);

    void updateStaffService(StaffServiceDTO staffServiceDTO);

    void deleteStaffService(long staffServiceId);

    StaffServiceDTO selectStaffService(long staffServiceId);

    List<StaffServiceDTO> getAllStaffServices();

    List<StaffServiceDTO> filterStaffServices(long staffServiceId, String staffName, String serviceName, Status staffServiceStatus);

    void changeStaffServiceStatus(long staffServiceId);

    List<StaffServiceDTO> getStaffServicesByStaffId(long staffId);

    List<StaffServiceDTO> getStaffServicesByServiceId(long serviceId);



}
