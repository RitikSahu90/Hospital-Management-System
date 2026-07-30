package hospital.management.backend.repository;

import hospital.management.backend.entity.DoctorAvailability;
import hospital.management.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctor(Doctor doctor);
}
