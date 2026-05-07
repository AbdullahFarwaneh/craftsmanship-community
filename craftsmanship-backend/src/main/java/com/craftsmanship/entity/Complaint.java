package com.craftsmanship.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "complaint")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Complaint {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Service service;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    // the complaint text – frontend calls it "complaint" not "comment"
    @Column(columnDefinition = "TEXT")
    private String complaint;

    // pending / resolved / rejected  (matches frontend exactly)
    @Builder.Default
    private String status = "pending";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }
}
