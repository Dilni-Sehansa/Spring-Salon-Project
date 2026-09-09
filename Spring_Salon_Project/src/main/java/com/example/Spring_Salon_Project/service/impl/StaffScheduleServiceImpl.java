package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.StaffScheduleDTO;
import com.example.Spring_Salon_Project.entity.*;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import com.example.Spring_Salon_Project.enumiration.StaffScheduleStatus;
import com.example.Spring_Salon_Project.enumiration.Status;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.StaffRepository;
import com.example.Spring_Salon_Project.repository.StaffScheduleRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.StaffScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffScheduleServiceImpl implements StaffScheduleService {

    private final StaffScheduleRepository staffScheduleRepository;
    private final StaffRepository staffRepository;
    private final AuditLogService auditLogService;

    @Override
    public StaffScheduleDTO saveSchedule(StaffScheduleDTO staffScheduleDTO) {
        log.info("Execute method saveSchedule");

        try{
            if (staffScheduleDTO.getStaffId() == null) {
                throw new CustomerException(400, "Staff ID is required");
            }

            Optional<Staff> optionalStaff = staffRepository.findById(staffScheduleDTO.getStaffId());
            if (optionalStaff.isEmpty()) {
                throw new CustomerException(404, "Staff not found ID: " + staffScheduleDTO.getStaffId());
            }

            StaffSchedule staffSchedule = new StaffSchedule();
            staffSchedule.setStaff(optionalStaff.get());
            staffSchedule.setDayOfWeek(staffScheduleDTO.getDayOfWeek());
            staffSchedule.setStartTime(staffScheduleDTO.getStartTime());
            staffSchedule.setEndTime(staffScheduleDTO.getEndTime());
            staffSchedule.setScheduleStatus(staffScheduleDTO.getScheduleStatus() != null ? staffScheduleDTO.getScheduleStatus() : StaffScheduleStatus.AVAILABLE);

            StaffSchedule save = staffScheduleRepository.save(staffSchedule);
            log.info("StaffSchedule saved successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("STAFF_SCHEDULE");
            logDTO.setEntityId(save.getScheduleId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("New schedule created for Staff ID: " + save.getStaff().getStaffId() + ", Day: " + save.getDayOfWeek());
            auditLogService.saveAuditLog(logDTO);

            return selectStaffSchedule(save.getScheduleId());

        } catch (Exception e) {
           log.error("Error saving StaffSchedule: {}", e.getMessage());
           throw new CustomerException(500, "Error saving StaffSchedule: " + e.getMessage());
        }
    }

    @Override
    public void updateSchedule(StaffScheduleDTO staffScheduleDTO) {
        Optional<StaffSchedule> optionalStaffSchedule = staffScheduleRepository.findById(staffScheduleDTO.getScheduleId());

        if (optionalStaffSchedule.isEmpty())
            throw new CustomerException(404, "Staff Schedule not found");

        StaffSchedule staffSchedule = optionalStaffSchedule.get();

        if (staffScheduleDTO.getStaffId() != null) {

            Optional<Staff> optionalStaff = staffRepository.findById(staffScheduleDTO.getStaffId());
            if (optionalStaff.isEmpty()) {
                throw new CustomerException(404, "Staff not found ID: " + staffScheduleDTO.getStaffId());
            }
            staffSchedule.setStaff(optionalStaff.get());
        }

        staffSchedule.setDayOfWeek(staffScheduleDTO.getDayOfWeek());
        staffSchedule.setStartTime(staffScheduleDTO.getStartTime());
        staffSchedule.setEndTime(staffScheduleDTO.getEndTime());

        if (staffScheduleDTO.getScheduleStatus() != null) {
            staffSchedule.setScheduleStatus(staffScheduleDTO.getScheduleStatus());
        }

        StaffSchedule updated = staffScheduleRepository.save(staffSchedule);
        log.info("StaffSchedule updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("STAFF_SCHEDULE");
        logDTO.setEntityId(updated.getScheduleId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Updated schedule ID: " + updated.getScheduleId() + " for Staff ID: " + updated.getStaff().getStaffId());
        auditLogService.saveAuditLog(logDTO);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        log.info("Execute method deleteSchedule() scheduleId{}",scheduleId);

        try{
            Optional<StaffSchedule> optionalStaffSchedule = staffScheduleRepository.findById(scheduleId);

            if (optionalStaffSchedule.isEmpty() || optionalStaffSchedule.get().getScheduleStatus() == StaffScheduleStatus.ON_LEAVE) {
                throw new CustomerException(404, "StaffSchedule not found ID: " + scheduleId);
            }

            StaffSchedule staffSchedule = optionalStaffSchedule.get();
            staffSchedule.setScheduleStatus(StaffScheduleStatus.ON_LEAVE);
            staffScheduleRepository.save(staffSchedule);

            log.info("staffSchedule marked as ON_LEAVE successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("STAFF_SCHEDULE");
            logDTO.setEntityId(staffSchedule.getScheduleId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Soft deleted schedule (set status to ON_LEAVE) ID: " + staffSchedule.getScheduleId());
            auditLogService.saveAuditLog(logDTO);

        } catch (Exception e) {
            log.error("Error deleting StaffSchedule: {}", e.getMessage());
            throw new CustomerException(500, "Error deleting StaffSchedule: " + e.getMessage());
        }
    }

    @Override
    public StaffScheduleDTO getScheduleById(Long scheduleId) {
        log.info("Execute method getScheduleById for ID: {}", scheduleId);
        StaffScheduleDTO dto = staffScheduleRepository.findByScheduleId(scheduleId);
        if (dto == null) {
            throw new CustomerException(404, "StaffSchedule not found ID: " + scheduleId);
        }
        return dto;
    }
    @Override
    public List<StaffScheduleDTO> getAllSchedules() {
        return staffScheduleRepository.getAllSchedule();
    }

    @Override
    public List<StaffScheduleDTO> getSchedulesByStaffId(Long staffId) {
        log.info("Execute method getSchedulesByStaffId for staffId: {}", staffId);
        return staffScheduleRepository.getStaffSchedulesByStaffId(staffId);
    }

    @Override
    public void updateScheduleStatus(Long scheduleId, String staffScheduleStatus) {
        log.info("Execute method updateScheduleStatus for ID: {} to Status: {}", scheduleId, staffScheduleStatus);

        Optional<StaffSchedule> optionalStaffSchedule = staffScheduleRepository.findById(scheduleId);

        if (optionalStaffSchedule.isEmpty()) {
            throw new CustomerException(404, "Staff Schedule not found");
        }

        try {
            StaffSchedule staffSchedule = optionalStaffSchedule.get();
            staffSchedule.setScheduleStatus(StaffScheduleStatus.valueOf(staffScheduleStatus.toUpperCase()));
            staffScheduleRepository.save(staffSchedule);
            log.info("Staff Schedule status updated successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("UPDATE");
            logDTO.setEntityName("STAFF_SCHEDULE");
            logDTO.setEntityId(staffSchedule.getScheduleId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Changed schedule status to " + staffSchedule.getScheduleStatus() + " for Schedule ID: " + scheduleId);
            auditLogService.saveAuditLog(logDTO);

        } catch (IllegalArgumentException e) {
            throw new CustomerException(400, "Invalid status: " + staffScheduleStatus);
        }
    }

    @Override
    public StaffScheduleDTO selectStaffSchedule(Long scheduleId) {
        Optional<StaffScheduleDTO> dto = staffScheduleRepository.selectStaffSchedule(scheduleId);
        if (dto.isEmpty()) {
            throw new CustomerException(404, "StaffSchedule not found ID: " + scheduleId);
        }
        return dto.get();
    }

    @Override
    public List<StaffScheduleDTO> filterStaffSchedules(long scheduleId, String staffName, DayOfWeek dayOfWeek, StaffScheduleStatus scheduleStatus) {
        return staffScheduleRepository.filterStaffSchedules(scheduleId, staffName, dayOfWeek, scheduleStatus);
    }
}
/*
* private Long scheduleId;
    private Long staffId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private StaffScheduleStatus scheduleStatus;*/
