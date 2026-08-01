package hospital.management.backend.repository;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.PatientReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    List<PatientReport> findByPatient(Patient patient);
}