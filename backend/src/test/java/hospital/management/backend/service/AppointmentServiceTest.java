package hospital.management.backend.service;

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
import hospital.management.backend.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void shouldBookAppointment() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setAppointmentDate(LocalDate.of(2026, 8, 1));
        request.setAppointmentTime(LocalTime.of(10, 0));

        Doctor doctor = Doctor.builder().id(2L).build();
        Patient patient = Patient.builder().id(1L).build();
        Appointment appointment = Appointment.builder().id(10L).doctor(doctor).patient(patient).status(AppointmentStatus.SCHEDULED).build();
        AppointmentResponse response = new AppointmentResponse(10L, 1L, 2L, request.getAppointmentDate(), request.getAppointmentTime(), AppointmentStatus.SCHEDULED, null);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), request.getAppointmentTime())).thenReturn(false);
        when(appointmentMapper.toEntity(request)).thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(response);

        AppointmentResponse result = appointmentService.bookAppointment(request);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldThrowWhenBookingAndDoctorNotFound() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1L);
        request.setDoctorId(99L);
        request.setAppointmentDate(LocalDate.of(2026, 8, 1));
        request.setAppointmentTime(LocalTime.of(10, 0));

        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.bookAppointment(request));

        assertThat(exception.getMessage()).isEqualTo("Doctor not found");
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenBookingAndPatientNotFound() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(99L);
        request.setDoctorId(2L);
        request.setAppointmentDate(LocalDate.of(2026, 8, 1));
        request.setAppointmentTime(LocalTime.of(10, 0));

        Doctor doctor = Doctor.builder().id(2L).build();
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.bookAppointment(request));

        assertThat(exception.getMessage()).isEqualTo("Patient not found");
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDoctorAlreadyHasAppointmentAtTime() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setAppointmentDate(LocalDate.of(2026, 8, 1));
        request.setAppointmentTime(LocalTime.of(10, 0));

        Doctor doctor = Doctor.builder().id(2L).build();
        Patient patient = Patient.builder().id(1L).build();

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, request.getAppointmentDate(), request.getAppointmentTime())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.bookAppointment(request));

        assertThat(exception.getMessage()).isEqualTo("Doctor already has an appointment at this time");
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldCancelAppointment() {
        Appointment appointment = Appointment.builder().id(11L).status(AppointmentStatus.SCHEDULED).build();
        when(appointmentRepository.findById(11L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(new AppointmentResponse(11L, 1L, 2L, LocalDate.now(), LocalTime.now(), AppointmentStatus.CANCELLED, null));

        AppointmentResponse result = appointmentService.cancelAppointment(11L);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldThrowWhenCancellingNonExistentAppointment() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.cancelAppointment(99L));

        assertThat(exception.getMessage()).isEqualTo("Appointment not found");
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldUpdateAppointmentStatus() {
        AppointmentStatusUpdateRequest request = new AppointmentStatusUpdateRequest();
        request.setStatus("completed");
        Appointment appointment = Appointment.builder().id(12L).status(AppointmentStatus.SCHEDULED).build();
        when(appointmentRepository.findById(12L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(new AppointmentResponse(12L, 1L, 2L, LocalDate.now(), LocalTime.now(), AppointmentStatus.COMPLETED, null));

        AppointmentResponse result = appointmentService.updateStatus(12L, request);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }

    @Test
    void shouldThrowWhenUpdatingStatusForNonExistentAppointment() {
        AppointmentStatusUpdateRequest request = new AppointmentStatusUpdateRequest();
        request.setStatus("completed");

        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateStatus(99L, request));

        assertThat(exception.getMessage()).isEqualTo("Appointment not found");
    }

    @Test
    void shouldThrowWhenUpdatingWithInvalidStatus() {
        AppointmentStatusUpdateRequest request = new AppointmentStatusUpdateRequest();
        request.setStatus("INVALID_STATUS");
        Appointment appointment = Appointment.builder().id(12L).status(AppointmentStatus.SCHEDULED).build();
        when(appointmentRepository.findById(12L)).thenReturn(Optional.of(appointment));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateStatus(12L, request));

        assertThat(exception.getMessage()).isEqualTo("Invalid appointment status");
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void shouldFindAppointmentById() {
        Appointment appointment = Appointment.builder().id(14L).status(AppointmentStatus.SCHEDULED).build();
        when(appointmentRepository.findById(14L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toResponse(appointment)).thenReturn(new AppointmentResponse(14L, 1L, 2L, LocalDate.now(), LocalTime.now(), AppointmentStatus.SCHEDULED, null));

        AppointmentResponse result = appointmentService.findById(14L);

        assertThat(result.getId()).isEqualTo(14L);
    }

    @Test
    void shouldThrowWhenFindingNonExistentAppointment() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.findById(99L));

        assertThat(exception.getMessage()).isEqualTo("Appointment not found");
    }

    @Test
    void shouldFindAllAppointments() {
        Appointment appointment = Appointment.builder().id(13L).build();
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentMapper.toResponse(appointment)).thenReturn(new AppointmentResponse(13L, 1L, 2L, LocalDate.now(), LocalTime.now(), AppointmentStatus.SCHEDULED, null));

        List<AppointmentResponse> result = appointmentService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenNoAppointments() {
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<AppointmentResponse> result = appointmentService.findAll();

        assertThat(result).isEmpty();
    }
}