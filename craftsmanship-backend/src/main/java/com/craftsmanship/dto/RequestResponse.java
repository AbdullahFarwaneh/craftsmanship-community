package com.craftsmanship.dto;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder
public class RequestResponse {
    private Long          id;
    private Long          serviceId;
    private String        customerName;
    private String        phoneNumber;
    private String        address;
    private String        status;       // pending / accepted / rejected
    private LocalDateTime createdAt;
}
