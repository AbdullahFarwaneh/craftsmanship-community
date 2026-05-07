package com.craftsmanship.repository;
import com.craftsmanship.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByServiceCraftsmanId(Long craftsmanId);
}
