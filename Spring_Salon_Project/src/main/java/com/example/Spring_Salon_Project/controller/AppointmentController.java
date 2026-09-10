package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/save-appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO savedAppointment = appointmentService.saveAppointment(appointmentDTO);
        return new CommonResponse(0, savedAppointment, "Appointment Saved Successfully");
    }

    @GetMapping(value = "/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllAppointments() {
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAllAppointments();
        return new CommonResponse(0, appointmentDTOs, "Appointments Loaded Successfully");
    }

    @GetMapping(value = "/select/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectAppointment(@PathVariable long appointmentId) {
        AppointmentDTO appointmentDTO = appointmentService.selectAppointment(appointmentId);
        return new CommonResponse(0, appointmentDTO, "Appointment Loaded Successfully");
    }

    @PutMapping(value = "/update-appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        appointmentService.updateAppointment(appointmentDTO);
        return new CommonResponse(0, "Appointment Updated Successfully");
    }

    @DeleteMapping(value = "/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteAppointment(@PathVariable long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return new CommonResponse(0, "Appointment Deleted Successfully");
    }

    @GetMapping(value = "/by-customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentsByCustomerId(@PathVariable Long customerId) {
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAppointmentsByCustomerId(customerId);
        return new CommonResponse(0, appointmentDTOs, "Appointments Loaded Successfully");
    }

    @GetMapping(value = "/by-date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAppointmentsByDate(date);
        return new CommonResponse(0, appointmentDTOs, "Appointments Loaded Successfully");
    }

    @GetMapping(value = "/by-status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAppointmentsByStatus(@PathVariable String status) {
        List<AppointmentDTO> appointmentDTOs = appointmentService.getAppointmentsByStatus(status);
        return new CommonResponse(0, appointmentDTOs, "Appointments Loaded Successfully");
    }

    @PatchMapping(value = "/update-status/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status) {
        appointmentService.updateAppointmentStatus(appointmentId, status);
        return new CommonResponse(0, "Appointment Status Updated Successfully");
    }

}