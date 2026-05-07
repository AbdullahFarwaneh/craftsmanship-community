package com.craftsmanship.service;

import com.craftsmanship.dto.*;
import com.craftsmanship.entity.Review;
import com.craftsmanship.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final ServiceRepository serviceRepo;

    public ReviewResponse submit(ReviewCreateDTO dto) {
        var service = serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Review r = Review.builder()
                .service(service)
                .customerName(dto.getCustomerName())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        return toDTO(reviewRepo.save(r));
    }

    public List<ReviewResponse> getByService(Long serviceId) {
        return reviewRepo.findByServiceId(serviceId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReviewResponse> myReviews(Long craftsmanId) {
        return reviewRepo.findByServiceCraftsmanId(craftsmanId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ReviewResponse toDTO(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .serviceId(r.getService().getId())
                .customerName(r.getCustomerName())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
