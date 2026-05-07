package com.craftsmanship.repository;
import com.craftsmanship.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByServiceCraftsmanId(Long craftsmanId);
}
