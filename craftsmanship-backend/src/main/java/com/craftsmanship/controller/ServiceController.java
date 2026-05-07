package com.craftsmanship.controller;

import com.craftsmanship.dto.*;
import com.craftsmanship.repository.CraftsmanRepository;
import com.craftsmanship.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController @RequestMapping("/api/services") @RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    private final CraftsmanRepository craftsmanRepo;

    // public
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAll() {
        return ResponseEntity.ok(serviceService.getAvailable());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ServiceResponse>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String city) {
        return ResponseEntity.ok(serviceService.search(q, city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getById(id));
    }

    // craftsman only
    @PostMapping
    public ResponseEntity<ServiceResponse> add(
            @RequestBody ServiceCreateDTO dto,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(serviceService.addService(craftsmanId(user), dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ServiceResponse>> my(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(serviceService.myServices(craftsmanId(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        serviceService.delete(id, craftsmanId(user));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceResponse> toggleStatus(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(serviceService.toggleStatus(id, craftsmanId(user)));
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<String> uploadMedia(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails user) throws IOException {
        return ResponseEntity.ok(serviceService.uploadMedia(id, craftsmanId(user), file));
    }

    private Long craftsmanId(UserDetails user) {
        return craftsmanRepo.findByEmail(user.getUsername())
                .or(() -> craftsmanRepo.findByPhoneNumber(user.getUsername()))
                .orElseThrow().getId();
    }
}
