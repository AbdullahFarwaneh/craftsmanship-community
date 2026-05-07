package com.craftsmanship.service;

import com.craftsmanship.dto.*;
import com.craftsmanship.entity.Service;
import com.craftsmanship.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private static final int MAX = 3;

    private final ServiceRepository serviceRepo;
    private final CraftsmanRepository craftsmanRepo;

    @Value("${upload.directory}")
    private String uploadDir;

    public List<ServiceResponse> getAvailable() {
        return serviceRepo.findByAvailableTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ServiceResponse> search(String q, String city) {
        return serviceRepo.search(
                (q != null && !q.isBlank()) ? q : null,
                (city != null && !city.isBlank()) ? city : null
        ).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ServiceResponse getById(Long id) {
        return toDTO(find(id));
    }

    public ServiceResponse addService(Long craftsmanId, ServiceCreateDTO dto) {
        if (serviceRepo.countByCraftsmanId(craftsmanId) >= MAX)
            throw new IllegalStateException("Maximum of " + MAX + " services per craftsman");

        var craftsman = craftsmanRepo.findById(craftsmanId)
                .orElseThrow(() -> new IllegalArgumentException("Craftsman not found"));

        String days = dto.getAvailableDays() != null
                ? String.join(",", dto.getAvailableDays()) : "";

        Service s = Service.builder()
                .craftsman(craftsman)
                .name(dto.getServiceName())
                .description(dto.getDescription())
                .serviceType(dto.getServiceType())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .availableDays(days)
                .availableTimeFrom(dto.getAvailableTimeFrom())
                .availableTimeTo(dto.getAvailableTimeTo())
                .available(true)
                .build();

        return toDTO(serviceRepo.save(s));
    }

    public List<ServiceResponse> myServices(Long craftsmanId) {
        return serviceRepo.findByCraftsmanId(craftsmanId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public void delete(Long serviceId, Long craftsmanId) {
        Service s = find(serviceId);
        if (!s.getCraftsman().getId().equals(craftsmanId))
            throw new SecurityException("Access denied");
        serviceRepo.delete(s);
    }

    public ServiceResponse toggleStatus(Long serviceId, Long craftsmanId) {
        Service s = find(serviceId);
        if (!s.getCraftsman().getId().equals(craftsmanId))
            throw new SecurityException("Access denied");
        s.setAvailable(!s.isAvailable());
        return toDTO(serviceRepo.save(s));
    }

    public String uploadMedia(Long serviceId, Long craftsmanId, MultipartFile file) throws IOException {
        Service s = find(serviceId);
        if (!s.getCraftsman().getId().equals(craftsmanId))
            throw new SecurityException("Access denied");

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        String url = "/uploads/" + filename;
        // first upload becomes the card image
        if (s.getImageUrl() == null) { s.setImageUrl(url); serviceRepo.save(s); }
        return url;
    }

    // ── mapper ────────────────────────────────────────────────────────────────
    private ServiceResponse toDTO(Service s) {
        List<String> days = (s.getAvailableDays() != null && !s.getAvailableDays().isBlank())
                ? Arrays.asList(s.getAvailableDays().split(","))
                : List.of();

        double avg = s.getReviews().stream()
                .mapToInt(r -> r.getRating()).average().orElse(0.0);
        double rating = Math.round(avg * 10.0) / 10.0;

        return ServiceResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .craftsmanName(s.getCraftsman().getFullName())
                .location(s.getAddress())          // frontend "location" = address
                .rating(rating)
                .reviewCount(s.getReviews().size())
                .imageUrl(s.getImageUrl())
                .available(s.isAvailable())
                .serviceType(s.getServiceType())
                .phone(s.getPhoneNumber())          // frontend "phone"
                .address(s.getAddress())
                .availableDays(days)
                .availableTime(new AvailableTime(s.getAvailableTimeFrom(), s.getAvailableTimeTo()))
                .build();
    }

    private Service find(Long id) {
        return serviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + id));
    }
}
