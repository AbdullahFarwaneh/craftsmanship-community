package com.craftsmanship.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonAlias({"identifier", "contact"})
    private String emailOrPhone;

    @JsonAlias({"emailAddress", "userEmail"})
    private String email;

    @JsonAlias({"phone", "phone_number", "mobileNumber"})
    private String phoneNumber;

    @NotBlank
    private String password;

    @JsonIgnore
    public String resolveIdentifier() {
        if (emailOrPhone != null && !emailOrPhone.isBlank()) {
            return emailOrPhone;
        }
        if (email != null && !email.isBlank()) {
            return email;
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            return phoneNumber;
        }
        return null;
    }
}
