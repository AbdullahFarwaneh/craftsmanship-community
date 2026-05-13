package com.craftsmanship.dto;
import lombok.Data;
@Data
public class ComplaintCreateDTO {
    private Long serviceId;
    private String customerName;
    private String complaint;   // frontend field name is "complaint"
}
