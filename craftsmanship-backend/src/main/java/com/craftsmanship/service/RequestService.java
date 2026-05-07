package com.craftsmanship.service;

import com.craftsmanship.dto.*;
import com.craftsmanship.entity.ServiceRequest;
import com.craftsmanship.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class RequestService {

    private final ServiceRequestRepository requestRepo;
    private final ServiceRepository serviceRepo;
    private final EmailService emailService;

    public RequestResponse submit(RequestCreateDTO dto) {
        var service = serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (!service.isAvailable())
            throw new IllegalStateException("This service is currently unavailable");

        ServiceRequest req = ServiceRequest.builder()
                .service(service)
                .customerName(dto.getCustomerName())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .status("pending")
                .build();

        req = requestRepo.save(req);
        emailService.notifyCraftsman(service, dto);
        return toDTO(req);
    }

    public List<RequestResponse> myRequests(Long craftsmanId) {
        return requestRepo.findByServiceCraftsmanId(craftsmanId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RequestResponse updateStatus(Long requestId, String status, Long craftsmanId) {
        ServiceRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (!req.getService().getCraftsman().getId().equals(craftsmanId))
            throw new SecurityException("Access denied");
        req.setStatus(status);
        return toDTO(requestRepo.save(req));
    }

    private RequestResponse toDTO(ServiceRequest r) {
        return RequestResponse.builder()
                .id(r.getId())
                .serviceId(r.getService().getId())
                .customerName(r.getCustomerName())
                .phoneNumber(r.getPhoneNumber())
                .address(r.getAddress())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
