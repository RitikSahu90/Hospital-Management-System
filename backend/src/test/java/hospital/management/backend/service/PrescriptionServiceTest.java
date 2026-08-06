package hospital.management.backend.service;

import hospital.management.backend.dto.request.PrescriptionItemRequest;
import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.MedicalRecord;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.mapper.PrescriptionMapper;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.MedicalRecordRepository;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import hospital.management.backend.service.impl.PrescriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {
    @Mock private PrescriptionRepository prescriptionRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private MedicalRecordRepository medicalRecordRepository;
    @Mock private MedicineRepository medicineRepository;
    @Mock private PrescriptionMapper prescriptionMapper;
    @Mock private software.amazon.awssdk.services.s3.S3Client s3Client;
    @Mock private software.amazon.awssdk.services.s3.presigner.S3Presigner s3Presigner;
    @Mock private hospital.management.backend.config.SecretsConfig secretsConfig;
    @InjectMocks private PrescriptionServiceImpl prescriptionService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    private PrescriptionRequest request() {
        PrescriptionItemRequest item = new PrescriptionItemRequest();
        item.setMedicineId(3L); item.setDosage("500mg"); item.setDurationDays(7); item.setQuantity(14);
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPatientId(1L); request.setDoctorId(2L); request.setMedicalRecordId(4L);
        request.setItems(List.of(item)); request.setNotes("Take with food");
        return request;
    }

    @Test void createsPrescriptionWithMedicalRecordItem() {
        Patient patient = new Patient(); Doctor doctor = new Doctor(); MedicalRecord record = new MedicalRecord(); Medicine medicine = new Medicine(); Prescription entity = new Prescription();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(medicalRecordRepository.findById(4L)).thenReturn(Optional.of(record));
        when(medicineRepository.findById(3L)).thenReturn(Optional.of(medicine));
        when(prescriptionMapper.toEntity(any())).thenReturn(entity);
        when(prescriptionRepository.save(entity)).thenReturn(entity);
        when(prescriptionMapper.toResponse(entity)).thenReturn(null);

        prescriptionService.create(request());

        assertSame(record, entity.getMedicalRecord());
        assertEquals(1, entity.getItems().size());
        assertSame(medicine, entity.getItems().get(0).getMedicine());
        verify(prescriptionRepository).save(entity);
    }

    @Test void rejectsMissingMedicalRecord() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(new Patient()));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(new Doctor()));
        when(medicalRecordRepository.findById(4L)).thenReturn(Optional.empty());
        when(prescriptionMapper.toEntity(any())).thenReturn(new Prescription());
        assertThrows(IllegalArgumentException.class, () -> prescriptionService.create(request()));
        verify(prescriptionRepository, never()).save(any());
    }
}
