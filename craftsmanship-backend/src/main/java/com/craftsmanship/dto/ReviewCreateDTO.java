package com.craftsmanship.dto;
import lombok.Data;
@Data
public class ReviewCreateDTO {
    private Long  serviceId;
    private String customerName;
    private int rating;
    private String comment;
}
