package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.SaloonServiceDTO;
import com.example.Spring_Salon_Project.enumiration.ServiceStatus;

import java.util.List;

public interface SaloonServiceService {

    SaloonServiceDTO saveSaloonService(SaloonServiceDTO saloonServiceDTO);

    void updateSaloonService(SaloonServiceDTO saloonServiceDTO);

    void deleteSaloonService(long serviceId);

    SaloonServiceDTO getSaloonServiceDetails(String serviceName);

    SaloonServiceDTO selectSaloonService(long serviceId);

    List<SaloonServiceDTO> getAllSaloonServices();

    List<SaloonServiceDTO> filterSaloonService(long serviceId, String serviceName, ServiceStatus serviceStatus);

    void changeSaloonStatus(long serviceId);

    SaloonServiceDTO getServiceByCategoryId(long categoryId);

}
