package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.CustomerDTO;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/customer-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody CustomerDTO customerDTO){
        CustomerDTO saveCustomer = customerService.saveCustomer(customerDTO);
        return new CommonResponse(0,saveCustomer,"Customer Saved Successfully");
    }

    @GetMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCustomers(){
        List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
        return new CommonResponse(0,customerDTOs,"Customer Loaded Successfully");
    }

    @GetMapping(value = "/filter-customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterCustomers(@RequestParam(value = "customerName", required = false) String customerName){
        List<CustomerDTO> customerDTOs = customerService.filterCustomers(customerName);
        return new CommonResponse(0,customerDTOs,"Customer Loaded Successfully");
    }

    @DeleteMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCustomer(@PathVariable long customerId){
        customerService.deleteCustomer(customerId);
        return new CommonResponse(0,"Customer Deleted Successfully");
    }

    @GetMapping(value = "/select-customer/{customerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectCustomer(@PathVariable long customerId){
        CustomerDTO customerDTO = customerService.selectCustomer(customerId);
        return new CommonResponse(0,customerDTO,"Customer Loaded Successfully");
    }

    @PutMapping(value = "/update-customer",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCustomer(@RequestBody CustomerDTO customerDTO){
        customerService.updateCustomer(customerDTO);
        return new CommonResponse(0,"Customer Updated Successfully");
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCustomerByUserId(@PathVariable long userId) {
        CustomerDTO customerDTO = customerService.getCustomerByUserId(userId);
        return new CommonResponse(0, customerDTO, "Customer Loaded Successfully");
    }

    @GetMapping(value = "/details/{customerName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCustomerDetails(@PathVariable String customerName) {
        CustomerDTO customerDTO = customerService.getCustomerDetails(customerName);
        return new CommonResponse(0, customerDTO, "Customer Loaded Successfully");
    }

    @GetMapping(value = "/by-phone/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCustomerByPhone(@PathVariable String phone) {
        CustomerDTO customerDTO = customerService.getCustomerByPhone(phone);
        return new CommonResponse(0, customerDTO, "Customer Loaded Successfully");
    }


}
