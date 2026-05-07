package com.craftsmanship.controller;

import com.craftsmanship.dto.*;
import com.craftsmanship.repository.CraftsmanRepository;
import com.craftsmanship.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/reviews") @RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final CraftsmanRepository craftsmanRepo;

    @PostMapping
    public ResponseEntity<ReviewResponse> submit(@RequestBody ReviewCreateDTO dto) {
        return ResponseEntity.ok(reviewService.submit(dto));
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<List<ReviewResponse>> byService(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getByService(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> my(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(reviewService.myReviews(craftsmanId(user)));
    }

    private Long craftsmanId(UserDetails user) {
        return craftsmanRepo.findByEmail(user.getUsername())
                .or(() -> craftsmanRepo.findByPhoneNumber(user.getUsername()))
                .orElseThrow().getId();
    }
}
