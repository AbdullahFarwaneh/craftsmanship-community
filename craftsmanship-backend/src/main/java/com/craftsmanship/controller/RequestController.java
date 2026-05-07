package com.craftsmanship.controller;

import com.craftsmanship.dto.*;
import com.craftsmanship.repository.CraftsmanRepository;
import com.craftsmanship.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/requests") @RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final CraftsmanRepository craftsmanRepo;

    // public – guest checkout
    @PostMapping
    public ResponseEntity<RequestResponse> submit(@RequestBody RequestCreateDTO dto) {
        return ResponseEntity.ok(requestService.submit(dto));
    }

    // craftsman – incoming requests
    @GetMapping("/my")
    public ResponseEntity<List<RequestResponse>> my(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(requestService.myRequests(craftsmanId(user)));
    }

    // craftsman – accept or reject a request
    @PatchMapping("/{id}/status")
    public ResponseEntity<RequestResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(requestService.updateStatus(id, status, craftsmanId(user)));
    }

    private Long craftsmanId(UserDetails user) {
        return craftsmanRepo.findByEmail(user.getUsername())
                .or(() -> craftsmanRepo.findByPhoneNumber(user.getUsername()))
                .orElseThrow().getId();
    }
}
