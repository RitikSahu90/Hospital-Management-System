package hospital.management.backend.service;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.mapper.PrescriptionMapper;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import hospital.management.backend.service.impl.PrescriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {
    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PrescriptionMapper prescriptionMapper;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePrescription() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setMedicineName("Amoxicillin");
        request.setDosage("500mg");
        request.setFrequency("Twice a day");
        request.setDurationDays(7);
        request.setPrescribedDate(LocalDate.now());
        request.setNotes("Take with food");

        Patient patient = new Patient();
        patient.setId(1L);
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        Prescription prescription = new Prescription();
        Prescription saved = new Prescription();
        saved.setId(1L);
        PrescriptionResponse response = new PrescriptionResponse(1L, 1L, 2L, "Amoxicillin", "500mg", "Twice a day", 7, request.getPrescribedDate(), "Take with food");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(prescriptionMapper.toEntity(request)).thenReturn(prescription);
        when(prescriptionRepository.save(prescription)).thenReturn(saved);
        when(prescriptionMapper.toResponse(saved)).thenReturn(response);

        PrescriptionResponse result = prescriptionService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Amoxicillin", result.getMedicineName());
        verify(prescriptionRepository).save(prescription);
    }

    @Test
    void shouldThrowWhenPatientNotFound() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setMedicineName("Amoxicillin");
        request.setDosage("500mg");
        request.setFrequency("Twice a day");
        request.setDurationDays(7);
        request.setPrescribedDate(LocalDate.now());

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prescriptionService.create(request));
        assertEquals("Patient not found", exception.getMessage());
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDoctorNotFoundOnCreate() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L);
        request.setDoctorId(99L);
        request.setMedicineName("Amoxicillin");
        request.setDosage("500mg");
        request.setFrequency("Twice a day");
        request.setDurationDays(7);
        request.setPrescribedDate(LocalDate.now());

        Patient patient = new Patient();
        patient.setId(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prescriptionService.create(request));
        assertEquals("Doctor not found", exception.getMessage());
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    void shouldUpdatePrescription() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setMedicineName("Ibuprofen");
        request.setDosage("400mg");
        request.setFrequency("Three times a day");
        request.setDurationDays(5);
        request.setPrescribedDate(LocalDate.now());
        request.setNotes("After meals");

        Patient patient = new Patient();
        patient.setId(1L);
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        PrescriptionResponse response = new PrescriptionResponse(1L, 1L, 2L, "Ibuprofen", "400mg", "Three times a day", 5, request.getPrescribedDate(), "After meals");

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);
        when(prescriptionMapper.toResponse(prescription)).thenReturn(response);

        PrescriptionResponse result = prescriptionService.update(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ibuprofen", result.getMedicineName());
        assertEquals("400mg", result.getDosage());
        verify(prescriptionRepository).save(prescription);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentPrescription() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setMedicineName("Amoxicillin");
        request.setDosage("500mg");
        request.setFrequency("Twice a day");
        request.setDurationDays(7);
        request.setPrescribedDate(LocalDate.now());

        when(prescriptionRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prescriptionService.update(99L, request));
        assertEquals("Prescription not found", exception.getMessage());
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingPrescriptionAndPatientNotFound() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(99L);
        request.setDoctorId(2L);
        request.setMedicineName("Amoxicillin");
        request.setDosage("500mg");
        request.setFrequency("Twice a day");
        request.setDurationDays(7);
        request.setPrescribedDate(LocalDate.now());

        Prescription prescription = new Prescription();
        prescription.setId(1L);

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prescriptionService.update(1L, request));
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUpdatingPrescriptionAndDoctorNotFound() {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L);
        request.setDoctorId(99L);
        request.setMedicineName("Amoxicillin");
        request.setDosage("500mg");
        request.setFrequency("Twice a day");
        request.setDurationDays(7);
        request.setPrescribedDate(LocalDate.now());

        Patient patient = new Patient();
        patient.setId(1L);
        Prescription prescription = new Prescription();
        prescription.setId(1L);

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prescriptionService.update(1L, request));
        assertEquals("Doctor not found", exception.getMessage());
    }

    @Test
    void shouldDeletePrescription() {
        prescriptionService.delete(1L);

        verify(prescriptionRepository).deleteById(1L);
    }

    @Test
    void shouldFindPrescriptionById() {
        Patient patient = new Patient();
        patient.setId(1L);
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionMapper.toResponse(prescription)).thenReturn(new PrescriptionResponse(1L, 1L, 2L, "Amoxicillin", "500mg", "Twice a day", 7, LocalDate.now(), null));

        PrescriptionResponse result = prescriptionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenFindingNonExistentPrescription() {
        when(prescriptionRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> prescriptionService.findById(99L));
        assertEquals("Prescription not found", exception.getMessage());
    }

    @Test
    void shouldFindAllPrescriptions() {
        Prescription prescription1 = new Prescription();
        prescription1.setId(1L);
        Prescription prescription2 = new Prescription();
        prescription2.setId(2L);

        when(prescriptionRepository.findAll()).thenReturn(List.of(prescription1, prescription2));
        when(prescriptionMapper.toResponse(prescription1)).thenReturn(new PrescriptionResponse(1L, 1L, 2L, "Amoxicillin", "500mg", "Twice a day", 7, LocalDate.now(), null));
        when(prescriptionMapper.toResponse(prescription2)).thenReturn(new PrescriptionResponse(2L, 1L, 2L, "Ibuprofen", "400mg", "Three times a day", 5, LocalDate.now(), null));

        List<PrescriptionResponse> result = prescriptionService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoPrescriptions() {
        when(prescriptionRepository.findAll()).thenReturn(Collections.emptyList());

        List<PrescriptionResponse> result = prescriptionService.findAll();

        assertTrue(result.isEmpty());
    }
}