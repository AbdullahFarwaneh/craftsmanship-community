package com.craftsmanship.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "craftsman")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Craftsman {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    // either email or phone – one must be present
    @Column(unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "craftsman", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude @Builder.Default
    private List<Service> services = new ArrayList<>();

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }
}
