package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.CustomerDTO;
import com.example.Spring_Salon_Project.dto.StaffDTO;
import com.example.Spring_Salon_Project.enumiration.StaffStatus;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.CustomerService;
import com.example.Spring_Salon_Project.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final JwtUtil jwtUtil;
    private final StaffService staffService;

    @PostMapping(value = "/staff-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveStaff(@RequestBody StaffDTO staffDTO){
        StaffDTO saveStaff = staffService.saveStaff(staffDTO);
        return new CommonResponse(0,saveStaff,"Staff Saved Successfully");
    }

    @PutMapping(value = "/update-staff",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStaff(@RequestBody StaffDTO staffDTO){
        staffService.updateStaff(staffDTO);
        return new CommonResponse(0,"Staff Updated Successfully");
    }

    @DeleteMapping(value = "/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteStaff(@PathVariable long staffId){
        staffService.deleteStaff(staffId);
        return new CommonResponse(0,"Staff Deleted Successfully");
    }

    @GetMapping(value = "/staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllStaff(){
        List<StaffDTO> staffDTO = staffService.getAllStaff();
        return new CommonResponse(0,staffDTO,"Staff Loaded Successfully");
    }

    @GetMapping(value = "/filter-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterStaff(@RequestParam(value = "specialization", required = false) String specialization){
        List<StaffDTO> staffDTO = staffService.filterStaff(specialization);
        return new CommonResponse(0,staffDTO,"Staff Loaded Successfully");
    }


    @GetMapping(value = "/select-staff/{staffId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectStaff(@PathVariable long staffId){
        StaffDTO staffDTO = staffService.selectStaff(staffId);
        return new CommonResponse(0,staffDTO,"Staff Loaded Successfully");
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStaffByUserId(@PathVariable long userId) {
        StaffDTO staffDTO = staffService.getStaffByUserId(userId);
        return new CommonResponse(0, staffDTO, "Staff Loaded Successfully");
    }

    @GetMapping(value = "/status/{staffStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStaffByStatus(@PathVariable StaffStatus staffStatus) {
        List<StaffDTO> staffDTOs = staffService.getStaffByStatus(staffStatus);
        return new CommonResponse(0, staffDTOs, "Staff Loaded Successfully");
    }
}
