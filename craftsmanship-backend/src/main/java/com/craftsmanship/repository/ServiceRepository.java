package com.craftsmanship.repository;
import com.craftsmanship.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCraftsmanId(Long craftsmanId);
    long countByCraftsmanId(Long craftsmanId);
    List<Service> findByAvailableTrue();

    @Query("SELECT s FROM Service s WHERE s.available = true " +
           "AND (:q IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(s.serviceType) LIKE LOWER(CONCAT('%',:q,'%'))) " +
           "AND (:city IS NULL OR LOWER(s.address) LIKE LOWER(CONCAT('%',:city,'%')))")
    List<Service> search(@Param("q") String q, @Param("city") String city);
}
