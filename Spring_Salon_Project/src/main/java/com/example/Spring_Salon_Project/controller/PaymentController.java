package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.PaymentDTO;
import com.example.Spring_Salon_Project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/save-payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse savePayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO savedPayment = paymentService.savePayment(paymentDTO);
        return new CommonResponse(201, savedPayment, "Payment Saved Successfully");
    }

    @GetMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllPayments() {
        List<PaymentDTO> allPayments = paymentService.getAllPayments();
        return new CommonResponse(200, allPayments, "Payments Loaded Successfully");
    }

    @GetMapping(value = "/appointment/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentByAppointmentId(@PathVariable Long appointmentId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByAppointmentId(appointmentId);
        return new CommonResponse(200, paymentDTO, "Payment Loaded Successfully");
    }

    @GetMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPaymentById(@PathVariable Long paymentId) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(paymentId);
        return new CommonResponse(200, paymentDTO, "Payment Loaded Successfully");
    }
}