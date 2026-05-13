package com.craftsmanship.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "service")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Service {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "craftsman_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Craftsman craftsman;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "phone_number")
    private String phoneNumber;


    private String address;

    @Builder.Default
    private boolean available = true;


    @Column(name = "available_days")
    private String availableDays;

    @Column(name = "available_time_from")
    private String availableTimeFrom;

    @Column(name = "available_time_to")
    private String availableTimeTo;


    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude @Builder.Default
    private List<ServiceRequest> requests = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude @Builder.Default
    private List<Complaint> complaints = new ArrayList<>();

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }
}
