package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.AppointmentRequest;
import hospital.management.backend.dto.request.AppointmentStatusUpdateRequest;
import hospital.management.backend.dto.response.AppointmentResponse;
import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.enums.AppointmentStatus;
import hospital.management.backend.mapper.AppointmentMapper;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper mapper;

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        if (appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new IllegalArgumentException("Doctor already has an appointment at this time");
        }

        Appointment appointment = mapper.toEntity(request);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return mapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return mapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateStatus(Long id, AppointmentStatusUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        AppointmentStatus status;
        try {
            status = AppointmentStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid appointment status");
        }
        appointment.setStatus(status);
        return mapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        return mapper.toResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}
