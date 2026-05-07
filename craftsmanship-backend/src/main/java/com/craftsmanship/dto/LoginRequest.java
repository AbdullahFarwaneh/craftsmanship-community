package com.craftsmanship.dto;
import lombok.Data;
@Data
public class LoginRequest {
    private String emailOrPhone;   // frontend sends "emailOrPhone"
    private String password;
}
