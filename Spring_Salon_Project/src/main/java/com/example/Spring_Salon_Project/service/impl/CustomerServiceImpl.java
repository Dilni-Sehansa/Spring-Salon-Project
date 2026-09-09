package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.CustomerDTO;
import com.example.Spring_Salon_Project.entity.Appointment;
import com.example.Spring_Salon_Project.entity.Customer;
import com.example.Spring_Salon_Project.entity.User;
import com.example.Spring_Salon_Project.enumiration.CustomerStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.CustomerRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Execute method saveCustomer");

        try {

            Customer customer = new Customer();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setPhone(customerDTO.getPhone());
            customer.setCustomerStatus(customerDTO.getCustomerStatus());

            if (customerDTO.getUserId() != null) {
                User user = new User();
                user.setUserId(customerDTO.getUserId());
                customer.setUser(user);
            }

            Customer save = customerRepository.save(customer);
            log.info("Customer saved successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("CUSTOMER");
            logDTO.setEntityId(save.getCustomerId());
            logDTO.setPerformedBy(save.getCustomerName());
            logDTO.setDetails("New customer created: " + save.getCustomerName());
            auditLogService.saveAuditLog(logDTO);

            Long savedUserId = (save.getUser() != null) ? save.getUser().getUserId() : null;

            return new CustomerDTO(save.getCustomerId(), save.getCustomerName(), save.getPhone(), save.getCustomerStatus(),savedUserId);

        } catch (Exception e) {
            log.error("Error saving customer: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerDTO.getCustomerId());

        if(optionalCustomer.isEmpty())
            throw new CustomerException(404,"Customer not found");

        Customer customer = optionalCustomer.get();
        customer.setCustomerName(customerDTO.getCustomerName());

        if(customerDTO.getPhone() != null && !customerDTO.getPhone().trim().isEmpty()){
            customer.setPhone(customerDTO.getPhone());
        }

        if (customerDTO.getCustomerStatus() != null) {
            customer.setCustomerStatus(customerDTO.getCustomerStatus());
        }

        if (customerDTO.getUserId() != null) {
            User user = new User();
            user.setUserId(customerDTO.getUserId());
            customer.setUser(user);
        }
        customerRepository.save(customer);
        log.info("Customer updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("CUSTOMER");
        logDTO.setEntityId(customer.getCustomerId());
        logDTO.setPerformedBy(customer.getCustomerName());
        logDTO.setDetails("Customer updated: " + customer.getCustomerName());
        auditLogService.saveAuditLog(logDTO);
    }

    @Override
    public CustomerDTO getCustomerDetails(String customerName) {

        log.info("Execute method getCustomerDetails");

        try {
            Optional<Customer> optionalCustomer = customerRepository.findByCustomerName(customerName);
            if(optionalCustomer.isEmpty())
                throw new CustomerException(404,"Customer not found");

            Customer customer = optionalCustomer.get();

            if (customer.getCustomerStatus() == CustomerStatus.INACTIVE) {
                throw new CustomerException(404,"Customer inactive");
            }

            return new CustomerDTO(customer.getCustomerId(),customer.getCustomerName(),customer.getPhone(),customer.getCustomerStatus(),(customer.getUser() != null) ? customer.getUser().getUserId() : null);

        }catch (Exception e){
            log.error("Error getting customer");
            throw e;
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @Override
    public List<CustomerDTO> filterCustomers(String customerName) {
        return customerRepository.filterCustomers(customerName);
    }

    @Override
    public CustomerDTO selectCustomer(long customerId) {

//        return customerRepository.selectCustomer(customerId);

        CustomerDTO customerDTO = customerRepository.selectCustomer(customerId);
        if (customerDTO == null) {
            throw new CustomerException(404, "Customer not found for ID: " + customerId);
        }
        return customerDTO;
    }

    @Override
    @Transactional
    public void deleteCustomer(long customerId) {

        log.info("Execute method deleteCustomer() customerId{}",customerId);

        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

            if (optionalCustomer.isEmpty() || optionalCustomer.get().getCustomerStatus() == CustomerStatus.INACTIVE) {
                throw new CustomerException(404, "Customer not found");
            }

            Customer customer = optionalCustomer.get();
            customer.setCustomerStatus(CustomerStatus.INACTIVE);
            customerRepository.save(customer);

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("CUSTOMER");
            logDTO.setEntityId(customer.getCustomerId());
            logDTO.setPerformedBy(customer.getCustomerName());
            logDTO.setDetails("Customer soft-deleted (INACTIVE): " + customer.getCustomerName());
            auditLogService.saveAuditLog(logDTO);

        }catch (Exception e){
            log.error("Error deleting customer");
            throw e;
        }

    }

    @Override
    public CustomerDTO getCustomerByUserId(long userId) {
        log.info("Execute method getCustomerByUserId for userId: {}", userId);

        Optional<CustomerDTO> customerDTO = customerRepository.getCustomerByUserId(userId);

        if (customerDTO.isEmpty()) {
            throw new CustomerException(404, "Customer not found for User ID: " + userId);
        }
        return customerDTO.get();
    }

    @Override
    public CustomerDTO getCustomerByPhone(String phone) {
        log.info("Execute method getCustomerByPhone for phone: {}", phone);

        Optional<CustomerDTO> optionalCustomer = customerRepository.findByPhone(phone);

        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        } else {
            throw new CustomerException(404, "Customer not found for phone number: " + phone);
        }
    }

    @Override
    public void updateCustomerStatus(Long customerId, CustomerStatus status) {
        log.info("Execute method updateCustomerStatus for ID: {} to Status: {}", customerId, status);

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            throw new CustomerException(404, "Customer not found");
        }
        Customer customer = optionalCustomer.get();
        customer.setCustomerStatus(status);
        customerRepository.save(customer);
        log.info("Customer status updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("CUSTOMER");
        logDTO.setEntityId(customer.getCustomerId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Appointment status changed to: " + status);
        auditLogService.saveAuditLog(logDTO);
    }
}