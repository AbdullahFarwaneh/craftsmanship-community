package com.craftsmanship.dto;
import lombok.Data;
@Data
public class RequestCreateDTO {
    private Long   serviceId;
    private String customerName;
    private String phoneNumber;
    private String address;
}
