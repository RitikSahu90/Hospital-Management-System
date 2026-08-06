package hospital.management.backend.repository;

import hospital.management.backend.entity.OtSchedule;
import hospital.management.backend.enums.OtStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtScheduleRepository extends JpaRepository<OtSchedule, Long> {
    List<OtSchedule> findByPatientId(Long patientId);
    List<OtSchedule> findBySurgeonId(Long surgeonId);
    List<OtSchedule> findByTheatreId(Long theatreId);
    List<OtSchedule> findByStatus(OtStatus status);
}
