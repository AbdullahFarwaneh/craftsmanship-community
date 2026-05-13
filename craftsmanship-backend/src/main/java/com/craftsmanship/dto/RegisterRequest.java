package com.craftsmanship.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @JsonAlias({"name", "full_name"})
    private String fullName;

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
        if (email != null && !email.isBlank()) {
            return email;
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            return phoneNumber;
        }
        if (emailOrPhone != null && !emailOrPhone.isBlank()) {
            return emailOrPhone;
        }

        return null;
    }

    @JsonIgnore
    public String resolveEmail() {
        if (email != null && !email.isBlank()) {
            return email;
        }
        if (emailOrPhone != null && !emailOrPhone.isBlank() && emailOrPhone.contains("@")) {
            return emailOrPhone;
        }
        return null;
    }

    @JsonIgnore
    public String resolvePhoneNumber() {
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            return phoneNumber;
        }
        if (emailOrPhone != null && !emailOrPhone.isBlank() && !emailOrPhone.contains("@")) {
            return emailOrPhone;
        }
        return null;
    }
}
