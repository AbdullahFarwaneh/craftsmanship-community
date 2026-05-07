package com.craftsmanship.controller;

import com.craftsmanship.dto.*;
import com.craftsmanship.repository.CraftsmanRepository;
import com.craftsmanship.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/complaints") @RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;
    private final CraftsmanRepository craftsmanRepo;

    @PostMapping
    public ResponseEntity<ComplaintResponse> submit(@RequestBody ComplaintCreateDTO dto) {
        return ResponseEntity.ok(complaintService.submit(dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ComplaintResponse>> my(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(complaintService.myComplaints(craftsmanId(user)));
    }

    // craftsman resolves or rejects a complaint
    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(complaintService.updateStatus(id, status, craftsmanId(user)));
    }

    private Long craftsmanId(UserDetails user) {
        return craftsmanRepo.findByEmail(user.getUsername())
                .or(() -> craftsmanRepo.findByPhoneNumber(user.getUsername()))
                .orElseThrow().getId();
    }
}
