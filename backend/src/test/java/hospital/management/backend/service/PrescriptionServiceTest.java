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
}
