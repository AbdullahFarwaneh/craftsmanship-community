package com.craftsmanship.dto;
import lombok.*;
@Data @AllArgsConstructor @Builder
public class AuthResponse {
    private Long   id;
    private String name;    // frontend uses "name"
    private String email;
    private String phone;   // frontend uses "phone"
    private String token;
}
