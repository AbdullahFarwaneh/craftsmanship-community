package com.craftsmanship.repository;
import com.craftsmanship.entity.Craftsman;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CraftsmanRepository extends JpaRepository<Craftsman, Long> {
    Optional<Craftsman> findByEmail(String email);
    Optional<Craftsman> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
