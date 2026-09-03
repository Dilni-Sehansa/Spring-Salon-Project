package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.StaffServiceDTO;
import com.example.Spring_Salon_Project.enumiration.Status;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.StaffServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/staffService")
@RequiredArgsConstructor
public class StaffServiceController {
    private final StaffServiceService staffServiceService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/staff-service-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveStaffService(@RequestBody StaffServiceDTO staffServiceDTO) {
        StaffServiceDTO savedStaffService = staffServiceService.saveStaffService(staffServiceDTO);
        return new CommonResponse(0, savedStaffService, "Staff Service Saved Successfully");
    }

    @PostMapping(value = "/save-multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveMultipleStaffServices(
            @RequestParam Long staffId,
            @RequestBody List<Long> serviceIds) {
        List<StaffServiceDTO> savedList = staffServiceService.saveMultipleStaffServices(staffId, serviceIds);
        return new CommonResponse(0, savedList, "Multiple Staff Services Saved Successfully");
    }

    @GetMapping(value = "/staff-service", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllStaffServices() {
        List<StaffServiceDTO> staffServiceDTOs = staffServiceService.getAllStaffServices();
        return new CommonResponse(0, staffServiceDTOs, "Staff Services Loaded Successfully");
    }

    @GetMapping(value = "/filter-staff-service", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterStaffServices(
            @RequestParam(value = "staffServiceId", defaultValue = "0") long staffServiceId,
            @RequestParam(value = "staffName", required = false) String staffName,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "status", required = false) Status status) {
        List<StaffServiceDTO> staffServiceDTOs = staffServiceService.filterStaffServices(staffServiceId, staffName, serviceName, status);
        return new CommonResponse(0, staffServiceDTOs, "Staff Services Loaded Successfully");
    }

    @DeleteMapping(value = "/{staffServiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteStaffService(@PathVariable long staffServiceId) {
        staffServiceService.deleteStaffService(staffServiceId);
        return new CommonResponse(0, "Staff Service Deleted Successfully");
    }

    @GetMapping(value = "/select-staff-service/{staffServiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectStaffService(@PathVariable long staffServiceId) {
        StaffServiceDTO staffServiceDTO = staffServiceService.selectStaffService(staffServiceId);
        return new CommonResponse(0, staffServiceDTO, "Staff Service Loaded Successfully");
    }

    @PutMapping(value = "/update-staff-service", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStaffService(@RequestBody StaffServiceDTO staffServiceDTO) {
        staffServiceService.updateStaffService(staffServiceDTO);
        return new CommonResponse(0, "Staff Service Updated Successfully");
    }

    @PatchMapping(value = "/change-status/{staffServiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse changeStaffServiceStatus(@PathVariable long staffServiceId) {
        staffServiceService.changeStaffServiceStatus(staffServiceId);
        return new CommonResponse(0, "Staff Service Status Changed Successfully");
    }

    @GetMapping(value = "/by-staff/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStaffServicesByStaffId(@PathVariable long staffId) {
        List<StaffServiceDTO> staffServiceDTOs = staffServiceService.getStaffServicesByStaffId(staffId);
        return new CommonResponse(0, staffServiceDTOs, "Staff Services Loaded Successfully");
    }

    @GetMapping(value = "/by-service/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStaffServicesByServiceId(@PathVariable long serviceId) {
        List<StaffServiceDTO> staffServiceDTOs = staffServiceService.getStaffServicesByServiceId(serviceId);
        return new CommonResponse(0, staffServiceDTOs, "Staff Services Loaded Successfully");
    }
}
