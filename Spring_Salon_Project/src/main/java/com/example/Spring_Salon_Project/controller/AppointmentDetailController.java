package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AppointmentDetailDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.AppointmentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/appointmentDetail")
@RequiredArgsConstructor
public class AppointmentDetailController {

    private final AppointmentDetailService appointmentDetailService;
    private final JwtUtil jwtUtil;

    @GetMapping(value = "/by-appointment/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDetailsByAppointmentId(@PathVariable Long appointmentId) {
        List<AppointmentDetailDTO> details = appointmentDetailService.getDetailsByAppointmentId(appointmentId);
        return new CommonResponse(0, details, "Appointment Details Loaded Successfully");
    }

    @GetMapping(value = "/select/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentDetailById(@PathVariable Long id) {
        AppointmentDetailDTO detail = appointmentDetailService.getAppointmentDetailById(id);
        return new CommonResponse(0, detail, "Appointment Detail Loaded Successfully");
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteAppointmentDetail(@PathVariable Long id) {
        appointmentDetailService.deleteAppointmentDetail(id);
        return new CommonResponse(0, "Appointment Detail Deleted Successfully");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentDetailsByPhoneAndCustomerName(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String customerName) {

        List<AppointmentDetailDTO> details = appointmentDetailService
                .getAppointmentDetailsByPhoneAndCustomerName(phone, customerName);

        return new CommonResponse(0, details, "Appointment Details Loaded Successfully");
    }
}