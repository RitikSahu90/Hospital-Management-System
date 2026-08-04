package hospital.management.backend.repository;

import hospital.management.backend.entity.EmergencyCase;
import hospital.management.backend.enums.EmergencyStatus;
import hospital.management.backend.enums.TriageLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyCaseRepository extends JpaRepository<EmergencyCase, Long> {
    List<EmergencyCase> findByPatientId(Long patientId);
    List<EmergencyCase> findByTriageLevel(TriageLevel triageLevel);
    List<EmergencyCase> findByStatus(EmergencyStatus status);
    long countByStatus(EmergencyStatus status);
}
