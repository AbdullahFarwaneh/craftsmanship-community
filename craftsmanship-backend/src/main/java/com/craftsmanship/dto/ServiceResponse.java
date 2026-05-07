package com.craftsmanship.dto;
import lombok.*;
import java.util.List;
// matches the Service interface in frontend mockData.ts exactly
@Data @Builder
public class ServiceResponse {
    private Long   id;
    private String name;
    private String craftsmanName;
    private String location;        // same as address – frontend uses "location" on card
    private double rating;
    private int    reviewCount;
    private String imageUrl;
    private boolean available;
    private String serviceType;
    private String phone;           // frontend uses "phone"
    private String address;
    private List<String> availableDays;
    private AvailableTime availableTime;  // { from, to }
}
