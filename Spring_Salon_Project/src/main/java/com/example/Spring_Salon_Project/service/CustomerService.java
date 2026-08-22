package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    void updateCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerDetails(String customerName);

    List<CustomerDTO> getAllCustomers();

    List<CustomerDTO> filterCustomers(String customerName);

    CustomerDTO selectCustomer(long customerId);

    void deleteCustomer(long customerId);

    CustomerDTO getCustomerByUserId(long userId);

    CustomerDTO getCustomerByPhone(String phone);
}
