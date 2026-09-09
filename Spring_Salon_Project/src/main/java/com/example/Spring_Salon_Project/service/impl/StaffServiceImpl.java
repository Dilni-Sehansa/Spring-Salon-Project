package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.StaffDTO;
import com.example.Spring_Salon_Project.entity.Appointment;
import com.example.Spring_Salon_Project.entity.Staff;
import com.example.Spring_Salon_Project.entity.User;
import com.example.Spring_Salon_Project.enumiration.StaffStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.StaffRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final AuditLogService auditLogService;

    @Override
    public StaffDTO saveStaff(StaffDTO staffDTO) {
        log.info("Execute method saveStaff");

        try{
            Staff staff = new Staff();
            staff.setSpecialization(staffDTO.getSpecialization());
            staff.setBio(staffDTO.getBio());
            staff.setExperienceYears(staffDTO.getExperienceYears());
//            staff.setStaffStatus(staffDTO.getStaffStatus());
            staff.setStaffStatus(staffDTO.getStaffStatus() != null ? staffDTO.getStaffStatus() : StaffStatus.AVAILABLE);

            if (staffDTO.getUserId() != null) {
                User user = new User();
                user.setUserId(staffDTO.getUserId());
                staff.setUser(user);
            }

            Staff save = staffRepository.save(staff);
            log.info("Staff saved successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("STAFF");
            logDTO.setEntityId(save.getStaffId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("New Staff member created with Specialization: " + save.getSpecialization());
            auditLogService.saveAuditLog(logDTO);

            Long savedUserId = (save.getUser() != null) ? save.getUser().getUserId() : null;
            String savedUserName = null;


            return new StaffDTO(save.getStaffId(),save.getSpecialization(),save.getBio(),save.getExperienceYears(),savedUserId, savedUserName ,save.getStaffStatus());

        } catch (Exception e) {
            log.error("Error saving staff: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateStaff(StaffDTO staffDTO) {
        Optional<Staff> optionalStaff = staffRepository.findById(staffDTO.getStaffId());

        if(optionalStaff.isEmpty())
                throw new CustomerException(404, "Staff not found");

        Staff staff = optionalStaff.get();
        staff.setSpecialization(staffDTO.getSpecialization());
        staff.setBio(staffDTO.getBio());
        staff.setExperienceYears(staffDTO.getExperienceYears());

        if(staffDTO.getStaffStatus() != null)
            staff.setStaffStatus(staffDTO.getStaffStatus());

        if (staffDTO.getUserId() != null) {
            User user = new User();
            user.setUserId(staffDTO.getUserId());
            staff.setUser(user);
        }

        Staff updatedStaff = staffRepository.save(staff);
        log.info("Staff updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("STAFF");
        logDTO.setEntityId(updatedStaff.getStaffId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Updated Staff member ID: " + updatedStaff.getStaffId() + ", Specialization: " + updatedStaff.getSpecialization());
        auditLogService.saveAuditLog(logDTO);
    }

    @Override
    public void deleteStaff(Long staffId) {

        log.info("Execute method deleteStaff() staffId: {}", staffId);

        try{
            Optional<Staff> optionalStaff = staffRepository.findById(staffId);

            if(optionalStaff.isEmpty() || optionalStaff.get().getStaffStatus()==StaffStatus.INACTIVE){
                throw new CustomerException(404, "Staff not found");
            }

            Staff staff = optionalStaff.get();
            staff.setStaffStatus(StaffStatus.INACTIVE);
            staffRepository.save(staff);

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("STAFF");
            logDTO.setEntityId(staff.getStaffId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Soft deleted (set status INACTIVE) Staff ID: " + staff.getStaffId());
            auditLogService.saveAuditLog(logDTO);

        } catch (Exception e) {
            log.error("Error delete staff");
            throw e;
        }

    }

    @Override
    public StaffDTO getStaffByUserId(long userId) {
        log.info("Execute method getStaffByUserId() userId: {}", userId);

        Optional<StaffDTO> staffDTO = staffRepository.getStaffByUserId(userId);

        if(staffDTO.isEmpty()){
            throw new CustomerException(404, "Staff not found for User Id: "+ userId);
        }
        return staffDTO.get();
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    @Override
    public List<StaffDTO> getStaffByStatus(StaffStatus staffStatus) {
        log.info("Execute method getStaffByStatus() staffStatus: {}", staffStatus);

        List<StaffDTO> staffList = staffRepository.findByStaffStatus(staffStatus);

        if (staffList.isEmpty()){
            throw new CustomerException(404, "Staff not found");
        }
        return staffList;
    }

    @Override
    public List<StaffDTO> filterStaff(String specialization) {
        return staffRepository.filterStaff(specialization);
    }

    @Override
    public StaffDTO selectStaff(long staffId) {
        StaffDTO staffDTO = staffRepository.selectStaff(staffId);
        if (staffDTO == null){
            throw new CustomerException(404, "Staff not found for Staff Id: "+ staffId);
        }
        return staffDTO;
    }

    @Override
    public void updateStaffStatus(Long staffId, StaffStatus status) {
        log.info("Execute method updateStaffStatus for ID: {} to Status: {}", staffId, status);

        Optional<Staff> optionalStaff = staffRepository.findById(staffId);

        if (optionalStaff.isEmpty()) {
            throw new CustomerException(404, "Staff not found");
        }
        Staff staff = optionalStaff.get();
        staff.setStaffStatus(status);
        staffRepository.save(staff);
        log.info("Staff status updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("STAFF");
        logDTO.setEntityId(staff.getStaffId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Staff status changed to: " + status);
        auditLogService.saveAuditLog(logDTO);
    }
}
