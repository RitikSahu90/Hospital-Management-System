package hospital.management.backend.repository;

import hospital.management.backend.entity.LabOrder;
import hospital.management.backend.enums.LabOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    Optional<LabOrder> findByOrderNumber(String orderNumber);
    List<LabOrder> findByPatientId(Long patientId);
    List<LabOrder> findByStatus(LabOrderStatus status);
    List<LabOrder> findByOrderedById(Long doctorId);
}
