package com.craftsmanship.service;

import com.craftsmanship.dto.*;
import com.craftsmanship.entity.Craftsman;
import com.craftsmanship.repository.CraftsmanRepository;
import com.craftsmanship.security.JwtUtil;
import lombok.RequiredArgsConstructor;
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
        String identifier = req.getEmailOrPhone();
        boolean isEmail = identifier != null && identifier.contains("@");

        if (isEmail && repo.existsByEmail(identifier))
            throw new IllegalArgumentException("Email already registered");
        if (!isEmail && repo.existsByPhoneNumber(identifier))
            throw new IllegalArgumentException("Phone number already registered");

        Craftsman c = Craftsman.builder()
                .fullName(req.getFullName())
                .email(isEmail ? identifier : null)
                .phoneNumber(isEmail ? null : identifier)
                .password(encoder.encode(req.getPassword()))
                .build();
        c = repo.save(c);

        String token = jwt.generate(identifier);
        return AuthResponse.builder()
                .id(c.getId()).name(c.getFullName())
                .email(c.getEmail()).phone(c.getPhoneNumber())
                .token(token).build();
    }

    public AuthResponse login(LoginRequest req) {
        String identifier = req.getEmailOrPhone();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, req.getPassword()));

        Craftsman c = repo.findByEmail(identifier)
                .or(() -> repo.findByPhoneNumber(identifier))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        String token = jwt.generate(identifier);
        return AuthResponse.builder()
                .id(c.getId()).name(c.getFullName())
                .email(c.getEmail()).phone(c.getPhoneNumber())
                .token(token).build();
    }
}
