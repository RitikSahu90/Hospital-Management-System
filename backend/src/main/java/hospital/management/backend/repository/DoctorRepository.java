package hospital.management.backend.repository;

import hospital.management.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    boolean existsByDoctorCode(String doctorCode);
    Optional<Doctor> findByUserUsername(String username);
}
