package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.SaloonServiceDTO;
import com.example.Spring_Salon_Project.entity.Category;
import com.example.Spring_Salon_Project.entity.SaloonService;
import com.example.Spring_Salon_Project.enumiration.ServiceStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.CategoryRepository;
import com.example.Spring_Salon_Project.repository.SaloonServiceRepository;
import com.example.Spring_Salon_Project.service.SaloonServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaloonServiceImpl implements SaloonServiceService {

    private final SaloonServiceRepository saloonServiceRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public SaloonServiceDTO saveSaloonService(SaloonServiceDTO saloonServiceDTO) {
        log.info("Execute method saveSaloonService");

        try {

            Optional<SaloonService> existingService = saloonServiceRepository.findByServiceName(saloonServiceDTO.getServiceName());
            if (existingService.isPresent()) {
                throw new CustomerException(400, "Service name already exists!");
            }

            Optional<Category> optionalCategory = categoryRepository.findById(saloonServiceDTO.getCategoryId());
            if (optionalCategory.isEmpty()) {
                throw new CustomerException(404, "Category not found!");
            }
            Category category = optionalCategory.get();

            SaloonService saloonService = new SaloonService();
            saloonService.setServiceName(saloonServiceDTO.getServiceName());
            saloonService.setDescription(saloonServiceDTO.getDescription());
            saloonService.setPrice(saloonServiceDTO.getPrice());
            saloonService.setDurationMinutes(saloonServiceDTO.getDurationMinutes());
            saloonService.setCategory(category);
            saloonService.setServiceStatus(saloonServiceDTO.getServiceStatus() != null ? saloonServiceDTO.getServiceStatus() : ServiceStatus.ACTIVE);

            SaloonService save = saloonServiceRepository.save(saloonService);
            log.info("SaloonService saved successfully");

            return new SaloonServiceDTO(save.getServiceId(),save.getServiceName(),save.getDescription(),save.getPrice(),save.getDurationMinutes(),save.getCategory().getCategoryId(),save.getCategory().getCategoryName(),save.getServiceStatus());

        }catch (Exception e){
            log.error("Error saving SaloonService: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateSaloonService(SaloonServiceDTO saloonServiceDTO) {
        Optional<SaloonService> optionalSaloonService = saloonServiceRepository.findById(saloonServiceDTO.getServiceId());

        if (optionalSaloonService.isEmpty()) {
            throw new CustomerException(404, "SaloonService not found!");
        }

        SaloonService saloonService = optionalSaloonService.get();
//        saloonService.setServiceName(saloonServiceDTO.getServiceName());

        if (saloonServiceDTO.getServiceName() != null && !saloonServiceDTO.getServiceName().equalsIgnoreCase(saloonService.getServiceName())) {
            Optional<SaloonService> existingService = saloonServiceRepository.findByServiceName(saloonServiceDTO.getServiceName());
            if (existingService.isPresent()) {
                throw new CustomerException(400, "Service name already exists!");
            }
            saloonService.setServiceName(saloonServiceDTO.getServiceName());
        }

        saloonService.setDescription(saloonServiceDTO.getDescription());
        saloonService.setPrice(saloonServiceDTO.getPrice());
        saloonService.setDurationMinutes(saloonServiceDTO.getDurationMinutes());

        if (saloonServiceDTO.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(saloonServiceDTO.getCategoryId());
            if (optionalCategory.isEmpty()) {
                throw new CustomerException(404, "Category not found!");
            }
            saloonService.setCategory(optionalCategory.get());
        }

        if(saloonServiceDTO.getServiceStatus() != null){
            saloonService.setServiceStatus(saloonServiceDTO.getServiceStatus());
        }
        saloonServiceRepository.save(saloonService);
        log.info("SaloonService updated successfully");
    }

    @Override
    public void deleteSaloonService(long serviceId) {
        log.info("Execute method deleteSaloonService");

        try {
            Optional<SaloonService> deleteService = saloonServiceRepository.findById(serviceId);
            if (deleteService.isEmpty() || deleteService.get().getServiceStatus() == ServiceStatus.INACTIVE){
                throw new CustomerException(404, "SaloonService not found!");
            }
            SaloonService saloonService = deleteService.get();
            saloonService.setServiceStatus(ServiceStatus.INACTIVE);
            saloonServiceRepository.save(saloonService);

        }catch (Exception e){
            log.error("Error deleting SaloonService: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public SaloonServiceDTO getSaloonServiceDetails(String serviceName) {
        log.info("Execute method getSaloonServiceDetails");

        try {
            Optional<SaloonService> getServices = saloonServiceRepository.findByServiceName(serviceName);
            if(getServices.isEmpty())
                throw new CustomerException(404,"Saloon Service not found");

            SaloonService saloonService = getServices.get();

            if (saloonService.getServiceStatus() == ServiceStatus.INACTIVE) {
                throw new CustomerException(404,"Saloon Service inactive");
            }

            return new SaloonServiceDTO(saloonService.getServiceId(),saloonService.getServiceName(),saloonService.getDescription(),saloonService.getPrice(),saloonService.getDurationMinutes(),(saloonService.getCategory() != null) ? saloonService.getCategory().getCategoryId() : null,(saloonService.getCategory() != null) ? saloonService.getCategory().getCategoryName() : null, saloonService.getServiceStatus());

        }catch (Exception e){
            log.error("Error getting saloon service details: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public SaloonServiceDTO selectSaloonService(long serviceId) {
        SaloonServiceDTO saloonServiceDTO = saloonServiceRepository.selectSaloonServices(serviceId);
        if (saloonServiceDTO == null){
            throw new CustomerException(404,"Saloon Service not found ID:"+serviceId);
        }
        return saloonServiceDTO;
    }

    @Override
    public List<SaloonServiceDTO> getAllSaloonServices() {
        return saloonServiceRepository.getAllSaloonServices();
    }

    @Override
    public List<SaloonServiceDTO> filterSaloonService(long serviceId, String serviceName, ServiceStatus serviceStatus) {
        return saloonServiceRepository.searchServices(serviceId, serviceName, serviceStatus);
    }

    @Override
    public void changeSaloonStatus(long serviceId) {
        log.info("Execute method changeSaloonStatus");
        Optional<SaloonService> optionalSaloonService = saloonServiceRepository.findById(serviceId);
        if (optionalSaloonService.isEmpty()) {
            throw new CustomerException(404, "Saloon Service not found ID:" + serviceId);
        }
        SaloonService saloonService = optionalSaloonService.get();
        if (saloonService.getServiceStatus() == ServiceStatus.ACTIVE) {
            saloonService.setServiceStatus(ServiceStatus.INACTIVE);
        } else {
            saloonService.setServiceStatus(ServiceStatus.ACTIVE);
        }
        saloonServiceRepository.save(saloonService);
        log.info("SaloonService status changed successfully");
    }

    @Override
    public SaloonServiceDTO getServiceByCategoryId(long categoryId) {
        log.info("Execute method getServiceByCategoryId for categoryId: {}", categoryId);

        Optional<SaloonServiceDTO> saloonServiceDTO = saloonServiceRepository.getServiceByCategoryId(categoryId);

        if (saloonServiceDTO.isEmpty()) {
            throw new CustomerException(404, "Services not found for Category ID: " + categoryId);
        }
        return saloonServiceDTO.get();
    }
}

/*
*  private Long serviceId;
    private String serviceName;
    private String description;
    private Double price;
    private Integer durationMinutes;
    private Category categoryId;
    private Category categoryName;
    private ServiceStatus serviceStatus;*/
