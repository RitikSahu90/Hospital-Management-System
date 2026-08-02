package hospital.management.backend.repository;

import hospital.management.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	java.util.Optional<Patient> findByUserUsername(String username);
}
