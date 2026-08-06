package hospital.management.backend.repository;

import hospital.management.backend.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    boolean existsByAppointmentId(Long appointmentId);
    java.util.Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
}