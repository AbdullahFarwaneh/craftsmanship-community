package com.craftsmanship.dto;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder
public class ReviewResponse {
    private Long  id;
    private Long   serviceId;
    private String    customerName;
    private int   rating;
    private String   comment;
    private LocalDateTime createdAt;
}
