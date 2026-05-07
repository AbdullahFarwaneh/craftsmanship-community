package com.craftsmanship.dto;
import lombok.Data;
import java.util.List;
@Data
public class ServiceCreateDTO {
    private String serviceName;        // frontend sends "serviceName"
    private String description;
    private String phoneNumber;
    private String serviceType;
    private String address;
    private List<String> availableDays; // frontend sends string[]
    private String availableTimeFrom;
    private String availableTimeTo;
}
