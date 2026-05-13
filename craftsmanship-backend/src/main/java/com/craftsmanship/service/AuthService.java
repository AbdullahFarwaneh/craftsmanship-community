package com.craftsmanship.service;

import com.craftsmanship.dto.*;
import com.craftsmanship.entity.Craftsman;
import com.craftsmanship.repository.CraftsmanRepository;
import com.craftsmanship.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {

    private final CraftsmanRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest req) {
        String fullName = normalize(req.getFullName());
        String email = normalize(req.resolveEmail());
        String phoneNumber = normalize(req.resolvePhoneNumber());
        String password = req.getPassword();

        if (fullName == null) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (email == null && phoneNumber == null) {
            throw new IllegalArgumentException("Email or phone number is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (email != null && repo.existsByEmail(email))
            throw new IllegalArgumentException("Email already registered");
        if (phoneNumber != null && repo.existsByPhoneNumber(phoneNumber))
            throw new IllegalArgumentException("Phone number already registered");

        Craftsman c = Craftsman.builder()
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(encoder.encode(password))
                .build();
        try {
            c = repo.save(c);
        } catch (DataIntegrityViolationException ex) {
            if (email != null && repo.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already registered");
            }
            if (phoneNumber != null && repo.existsByPhoneNumber(phoneNumber)) {
                throw new IllegalArgumentException("Phone number already registered");
            }
            throw new IllegalArgumentException("Registration data is invalid or already exists");
        }

        String token = jwt.generate(getPrincipal(c));
        return AuthResponse.builder()
                .id(c.getId()).name(c.getFullName())
                .email(c.getEmail()).phone(c.getPhoneNumber())
                .token(token).build();
    }

    public AuthResponse login(LoginRequest req) {
        String identifier = normalize(req.resolveIdentifier());
        String password = req.getPassword();

        if (identifier == null) {
            throw new IllegalArgumentException("Email or phone number is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));

        Craftsman c = repo.findByEmail(identifier)
                .or(() -> repo.findByPhoneNumber(identifier))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        String token = jwt.generate(getPrincipal(c));
        return AuthResponse.builder()
                .id(c.getId()).name(c.getFullName())
                .email(c.getEmail()).phone(c.getPhoneNumber())
                .token(token).build();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String getPrincipal(Craftsman craftsman) {
        String email = normalize(craftsman.getEmail());
        return email != null ? email : normalize(craftsman.getPhoneNumber());
    }
}
