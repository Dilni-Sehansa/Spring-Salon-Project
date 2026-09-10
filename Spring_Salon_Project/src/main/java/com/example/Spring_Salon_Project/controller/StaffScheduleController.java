package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.StaffScheduleDTO;
import com.example.Spring_Salon_Project.enumiration.StaffScheduleStatus;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.StaffScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/staffSchedule")
@RequiredArgsConstructor
public class StaffScheduleController {
    private final StaffScheduleService staffScheduleService;
    private final JwtUtil jwtUtil;


    @PostMapping(value = "/staff-schedule-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveStaffSchedule(@Valid @RequestBody StaffScheduleDTO staffScheduleDTO) {
        StaffScheduleDTO savedStaffSchedule = staffScheduleService.saveSchedule(staffScheduleDTO);
        return new CommonResponse(0, savedStaffSchedule, "Staff Schedule Saved Successfully");
    }

    @GetMapping(value = "/staff-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllStaffSchedules() {
        List<StaffScheduleDTO> staffScheduleDTOS = staffScheduleService.getAllSchedules();
        return new CommonResponse(0, staffScheduleDTOS, "Staff Schedule Loaded Successfully");
    }

    @DeleteMapping(value = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteStaffSchedule(@PathVariable long scheduleId) {
        staffScheduleService.deleteSchedule(scheduleId);
        return new CommonResponse(0, "Staff Schedule Deleted Successfully");
    }

    @PutMapping(value = "/update-staff-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStaffSchedule(@Valid @RequestBody StaffScheduleDTO staffScheduleDTO) {
        staffScheduleService.updateSchedule(staffScheduleDTO);
        return new CommonResponse(0, "Staff Schedule Updated Successfully");
    }

    @GetMapping(value = "/select-staff-schedule/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectStaffSchedule(@PathVariable long scheduleId) {
        StaffScheduleDTO selectStaffSchedule = staffScheduleService.selectStaffSchedule(scheduleId);
        return new CommonResponse(0, selectStaffSchedule, "Staff Schedule Loaded Successfully");
    }

    @PatchMapping(value = "/change-status/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStaffScheduleStatus(
            @PathVariable Long scheduleId,
            @RequestParam String staffScheduleStatus) {
        staffScheduleService.updateScheduleStatus(scheduleId, staffScheduleStatus);
        return new CommonResponse(0, "Staff Schedule Status Updated Successfully");

    }

    @GetMapping(value = "/staff/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getScheduleByStaffId(@PathVariable long staffId) {
        List<StaffScheduleDTO> staffScheduleDTOs = staffScheduleService.getSchedulesByStaffId(staffId);
        return new CommonResponse(0, staffScheduleDTOs, "Staff Schedules Loaded Successfully");
    }

    @GetMapping(value = "/filter-staff-schedules", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterStaffSchedules(
            @RequestParam(value = "scheduleId", defaultValue = "0") long scheduleId,
            @RequestParam(value = "staffName", required = false) String staffName,
            @RequestParam(value = "dayOfWeek", required = false) DayOfWeek dayOfWeek,
            @RequestParam(value = "scheduleStatus", required = false) StaffScheduleStatus scheduleStatus) {
        List<StaffScheduleDTO> staffScheduleDTOs = staffScheduleService.filterStaffSchedules(scheduleId, staffName, dayOfWeek, scheduleStatus);
        return new CommonResponse(0, staffScheduleDTOs, "Staff Schedules Loaded Successfully");
    }

    @GetMapping(value = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getScheduleById(@PathVariable Long scheduleId) {
        StaffScheduleDTO staffScheduleDTO = staffScheduleService.getScheduleById(scheduleId);
        return new CommonResponse(0, staffScheduleDTO, "Staff Schedule Loaded Successfully");
    }

}
