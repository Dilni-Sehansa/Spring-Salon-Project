package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.StaffScheduleDTO;
import com.example.Spring_Salon_Project.enumiration.StaffScheduleStatus;

import java.time.DayOfWeek;
import java.util.List;

public interface StaffScheduleService {
    StaffScheduleDTO saveSchedule(StaffScheduleDTO staffScheduleDTO);
    void updateSchedule(StaffScheduleDTO staffScheduleDTO);
    void deleteSchedule(Long scheduleId);
    StaffScheduleDTO getScheduleById(Long scheduleId);
    List<StaffScheduleDTO> getAllSchedules();
    List<StaffScheduleDTO> getSchedulesByStaffId(Long staffId);
//    StaffScheduleDTO getSchedulesByStaffId(Long staffId);
    void updateScheduleStatus(Long scheduleId, String staffScheduleStatus);
    StaffScheduleDTO selectStaffSchedule(Long scheduleId);
    List<StaffScheduleDTO> filterStaffSchedules(long scheduleId, String staffName, DayOfWeek dayOfWeek, StaffScheduleStatus scheduleStatus);
}
