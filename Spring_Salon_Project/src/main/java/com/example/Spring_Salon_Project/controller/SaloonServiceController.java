package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.SaloonServiceDTO;
import com.example.Spring_Salon_Project.enumiration.ServiceStatus;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.SaloonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/service")
@RequiredArgsConstructor
public class SaloonServiceController {
    private final SaloonServiceService saloonServiceService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/service-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveService(@RequestBody SaloonServiceDTO saloonServiceDTO){
        SaloonServiceDTO saveService = saloonServiceService.saveSaloonService(saloonServiceDTO);
        return new CommonResponse(0,saveService,"Service Saved Successfully");
    }

    @GetMapping(value = "/service", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllSaloonService(){
        List<SaloonServiceDTO> getAllServices = saloonServiceService.getAllSaloonServices();
        return new CommonResponse(0,getAllServices,"Service Loaded Successfully");
    }

    @GetMapping(value = "/filter-service", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterService(
            @RequestParam(value = "serviceId", required = false, defaultValue = "0") long serviceId,
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "serviceStatus", required = false) ServiceStatus serviceStatus
    ) {
        List<SaloonServiceDTO> saloonServiceDTOS = saloonServiceService.filterSaloonService(serviceId, serviceName, serviceStatus);
        return new CommonResponse(0, saloonServiceDTOS, "Service Loaded Successfully");
    }

    @DeleteMapping(value = "/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteService(@PathVariable long serviceId){
        saloonServiceService.deleteSaloonService(serviceId);
        return new CommonResponse(0,"Service Deleted Successfully");
    }

    @GetMapping(value = "/select-service/{serviceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectService(@PathVariable long serviceId){
        SaloonServiceDTO selectService = saloonServiceService.selectSaloonService(serviceId);
        return new CommonResponse(0,selectService,"Service Loaded Successfully");
    }

    @PutMapping(value = "/update-service",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateService(@RequestBody SaloonServiceDTO saloonServiceDTO){
        saloonServiceService.updateSaloonService(saloonServiceDTO);
        return new CommonResponse(0,"Service Updated Successfully");
    }

    @GetMapping(value = "/details/{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getServiceDetails(@PathVariable String serviceName) {
        SaloonServiceDTO saloonServiceDTO = saloonServiceService.getSaloonServiceDetails(serviceName);
        return new CommonResponse(0, saloonServiceDTO, "Services Loaded Successfully");
    }

    @PatchMapping(value = "/change-status/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse changeSaloonStatus(@PathVariable long serviceId) {
        saloonServiceService.changeSaloonStatus(serviceId);
        return new CommonResponse(0, "Service Status Changed Successfully");
    }
}
