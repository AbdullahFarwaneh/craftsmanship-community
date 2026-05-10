package com.craftsmanship.dto;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder
public class ComplaintResponse {
    private Long  id;
    private String  customerName;
    private Long   serviceId;
    private String  serviceName;   // frontend shows serviceName on card
    private String  complaint;
    private String  status;        // pending / resolved / rejected
    private LocalDateTime createdAt;
}
