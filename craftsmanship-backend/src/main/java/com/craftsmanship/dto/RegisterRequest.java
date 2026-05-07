package com.craftsmanship.dto;
import lombok.Data;
@Data
public class RegisterRequest {
    private String fullName;
    private String emailOrPhone;   // frontend sends "emailOrPhone"
    private String password;
}
