package hospital.management.backend.repository;

import hospital.management.backend.entity.OpdIpdVisit;
import hospital.management.backend.enums.VisitStatus;
import hospital.management.backend.enums.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpdIpdVisitRepository extends JpaRepository<OpdIpdVisit, Long> {
    List<OpdIpdVisit> findByPatientId(Long patientId);
    List<OpdIpdVisit> findByVisitType(VisitType visitType);
    List<OpdIpdVisit> findByStatus(VisitStatus status);
    List<OpdIpdVisit> findByVisitTypeAndStatus(VisitType visitType, VisitStatus status);
    long countByStatus(VisitStatus status);
}
