package com.craftsmanship.service;

import com.craftsmanship.dto.*;
import com.craftsmanship.entity.Complaint;
import com.craftsmanship.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepo;
    private final ServiceRepository serviceRepo;

    public ComplaintResponse submit(ComplaintCreateDTO dto) {
        var service = serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Complaint c = Complaint.builder()
                .service(service)
                .customerName(dto.getCustomerName())
                .complaint(dto.getComplaint())
                .status("pending")
                .build();

        return toDTO(complaintRepo.save(c));
    }

    public List<ComplaintResponse> myComplaints(Long craftsmanId) {
        return complaintRepo.findByServiceCraftsmanId(craftsmanId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ComplaintResponse updateStatus(Long complaintId, String status, Long craftsmanId) {
        Complaint c = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found"));
        if (!c.getService().getCraftsman().getId().equals(craftsmanId))
            throw new SecurityException("Access denied");
        c.setStatus(status);
        return toDTO(complaintRepo.save(c));
    }

    private ComplaintResponse toDTO(Complaint c) {
        return ComplaintResponse.builder()
                .id(c.getId())
                .customerName(c.getCustomerName())
                .serviceId(c.getService().getId())
                .serviceName(c.getService().getName())
                .complaint(c.getComplaint())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
