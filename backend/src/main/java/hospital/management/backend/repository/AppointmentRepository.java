package hospital.management.backend.repository;

import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(Doctor doctor, LocalDate appointmentDate, LocalTime appointmentTime);
    List<Appointment> findByDoctor(Doctor doctor);
}
