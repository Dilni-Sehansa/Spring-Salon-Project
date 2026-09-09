package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.StaffServiceDTO;
import com.example.Spring_Salon_Project.entity.SaloonService;
import com.example.Spring_Salon_Project.entity.Staff;
import com.example.Spring_Salon_Project.entity.StaffService;
import com.example.Spring_Salon_Project.enumiration.Status;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.SaloonServiceRepository;
import com.example.Spring_Salon_Project.repository.StaffRepository;
import com.example.Spring_Salon_Project.repository.StaffServiceRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.StaffServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class StaffServiceServiceImpl implements StaffServiceService {

    private final StaffServiceRepository staffServiceRepository;
    private final SaloonServiceRepository saloonServiceRepository;
    private final StaffRepository staffRepository;
    private final AuditLogService auditLogService;


    @Override
    public StaffServiceDTO saveStaffService(StaffServiceDTO staffServiceDTO) {
        log.info("Execute method saveStaffService");

        try {
            Optional<Staff> optionalStaff = staffRepository.findById(staffServiceDTO.getStaffId());
            if (optionalStaff.isEmpty()) {
                throw new CustomerException(404, "Staff not found ID: " + staffServiceDTO.getStaffId());
            }

            Optional<SaloonService> optionalService = saloonServiceRepository.findById(staffServiceDTO.getServiceId());
            if (optionalService.isEmpty()) {
                throw new CustomerException(404, "Saloon Service not found ID: " + staffServiceDTO.getServiceId());
            }

            Optional<StaffService> existingMapping = staffServiceRepository
                    .findByStaffStaffIdAndSaloonServiceServiceId(staffServiceDTO.getStaffId(), staffServiceDTO.getServiceId());
            if (existingMapping.isPresent()) {
                throw new CustomerException(400, "This Service is already assigned to the Staff!");
            }

            StaffService staffService = new StaffService();
            staffService.setStaff(optionalStaff.get());
            staffService.setSaloonService(optionalService.get());
            staffService.setStaffServiceStatus(staffServiceDTO.getStaffServiceStatus() != null ? staffServiceDTO.getStaffServiceStatus() : Status.ACTIVE);

            StaffService saved = staffServiceRepository.save(staffService);
            log.info("StaffService saved successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("STAFF_SERVICE");
            logDTO.setEntityId(saved.getStaffServiceId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Assigned Service ID: " + saved.getSaloonService().getServiceId() + " to Staff ID: " + saved.getStaff().getStaffId());
            auditLogService.saveAuditLog(logDTO);

            return selectStaffService(saved.getStaffServiceId());

        } catch (Exception e) {
            log.error("Error saving StaffService: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<StaffServiceDTO> saveMultipleStaffServices(Long staffId, List<Long> serviceIds) {
        return List.of();
    }

    @Override
    public void updateStaffService(StaffServiceDTO staffServiceDTO) {
        log.info("Execute method updateStaffService");

        Optional<StaffService> optionalStaffService = staffServiceRepository.findById(staffServiceDTO.getStaffServiceId());

        if (optionalStaffService.isEmpty()) {
            throw new CustomerException(404, "StaffService not found ID: " + staffServiceDTO.getStaffServiceId());
        }

        StaffService staffService = optionalStaffService.get();

        if (staffServiceDTO.getStaffId() != null) {
            Optional<Staff> optionalStaff = staffRepository.findById(staffServiceDTO.getStaffId());
            if (optionalStaff.isEmpty()) {
                throw new CustomerException(404, "Staff not found ID: " + staffServiceDTO.getStaffId());
            }
            staffService.setStaff(optionalStaff.get());
        }

        if (staffServiceDTO.getServiceId() != null) {
            Optional<SaloonService> optionalService = saloonServiceRepository.findById(staffServiceDTO.getServiceId());
            if (optionalService.isEmpty()) {
                throw new CustomerException(404, "Saloon Service not found ID: " + staffServiceDTO.getServiceId());
            }
            staffService.setSaloonService(optionalService.get());
        }

        if (staffServiceDTO.getStaffServiceStatus() != null) {
            staffService.setStaffServiceStatus(staffServiceDTO.getStaffServiceStatus());
        }

        staffServiceRepository.save(staffService);
        log.info("StaffService updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("STAFF_SERVICE");
        logDTO.setEntityId(staffService.getStaffServiceId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Updated StaffService mapping ID: " + staffService.getStaffServiceId());
        auditLogService.saveAuditLog(logDTO);

    }

    @Override
    public void deleteStaffService(long staffServiceId) {
        log.info("Execute method deleteStaffService");

        try {
            Optional<StaffService> optionalStaffService = staffServiceRepository.findById(staffServiceId);
            if (optionalStaffService.isEmpty() || optionalStaffService.get().getStaffServiceStatus() == Status.INACTIVE) {
                throw new CustomerException(404, "StaffService not found ID: " + staffServiceId);
            }

            StaffService staffService = optionalStaffService.get();
            staffService.setStaffServiceStatus(Status.INACTIVE);
            staffServiceRepository.save(staffService);

            log.info("StaffService marked as INACTIVE successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("STAFF_SERVICE");
            logDTO.setEntityId(staffService.getStaffServiceId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Soft deleted StaffService mapping ID: " + staffService.getStaffServiceId());
            auditLogService.saveAuditLog(logDTO);

        } catch (Exception e) {
            log.error("Error deleting StaffService: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public StaffServiceDTO selectStaffService(long staffServiceId) {
        Optional<StaffServiceDTO> dto = staffServiceRepository.selectStaffService(staffServiceId);
        if (dto.isEmpty()) {
            throw new CustomerException(404, "StaffService not found ID: " + staffServiceId);
        }
        return dto.get();
    }

    @Override
    public List<StaffServiceDTO> getAllStaffServices() {
        return staffServiceRepository.getAllStaffServices();
    }

    @Override
    public List<StaffServiceDTO> filterStaffServices(long staffServiceId, String staffName, String serviceName, Status staffServiceStatus) {
        return staffServiceRepository.filterStaffServices(staffServiceId, staffName, serviceName, staffServiceStatus);
    }

    @Override
    public void changeStaffServiceStatus(long staffServiceId) {
        log.info("Execute method changeStaffServiceStatus");

        Optional<StaffService> optionalStaffService = staffServiceRepository.findById(staffServiceId);
        if (optionalStaffService.isEmpty()) {
            throw new CustomerException(404, "StaffService not found ID: " + staffServiceId);
        }

        StaffService staffService = optionalStaffService.get();
        if (staffService.getStaffServiceStatus() == Status.ACTIVE) {
            staffService.setStaffServiceStatus(Status.INACTIVE);
        } else {
            staffService.setStaffServiceStatus(Status.ACTIVE);
        }

        staffServiceRepository.save(staffService);
        log.info("StaffService status changed successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("STAFF_SERVICE");
        logDTO.setEntityId(staffService.getStaffServiceId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Changed StaffService status to: " + staffService.getStaffServiceStatus() + " for ID: " + staffService.getStaffServiceId());
        auditLogService.saveAuditLog(logDTO);
    }

    @Override
    public List<StaffServiceDTO> getStaffServicesByStaffId(long staffId) {
        return staffServiceRepository.getStaffServicesByStaffId(staffId);
    }

    @Override
    public List<StaffServiceDTO> getStaffServicesByServiceId(long serviceId) {
        return staffServiceRepository.getStaffServicesByServiceId(serviceId);
    }
}
