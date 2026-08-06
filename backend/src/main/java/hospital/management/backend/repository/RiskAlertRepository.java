package hospital.management.backend.repository;

import hospital.management.backend.entity.RiskAlert;
import hospital.management.backend.enums.AlertSeverity;
import hospital.management.backend.enums.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskAlertRepository extends JpaRepository<RiskAlert, Long> {
    List<RiskAlert> findByPatientId(Long patientId);
    List<RiskAlert> findByIsAcknowledged(Boolean isAcknowledged);
    List<RiskAlert> findBySeverity(AlertSeverity severity);
    List<RiskAlert> findByAlertType(AlertType alertType);
    List<RiskAlert> findByIsAcknowledgedFalse();
}
