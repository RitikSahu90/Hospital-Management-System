package hospital.management.backend.repository;

import hospital.management.backend.entity.LabTest;
import hospital.management.backend.enums.LabCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByCategory(LabCategory category);
    List<LabTest> findByIsActive(Boolean isActive);
}
