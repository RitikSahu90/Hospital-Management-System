package hospital.management.backend.service;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.entity.Billing;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.mapper.BillingMapper;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import hospital.management.backend.service.impl.BillingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillingServiceTest {
    @Mock
    private BillingRepository billingRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private BillingMapper billingMapper;

    @InjectMocks
    private BillingServiceImpl billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateBilling() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(1L);
        request.setPrescriptionId(2L);
        request.setTotalAmount(BigDecimal.valueOf(100.0));
        request.setPaidAmount(BigDecimal.valueOf(80.0));
        request.setBillingDate(LocalDate.now());

        Patient patient = new Patient();
        patient.setId(1L);
        Prescription prescription = new Prescription();
        prescription.setId(2L);
        Billing billing = new Billing();
        Billing saved = new Billing();
        saved.setId(1L);
        BillingResponse response = new BillingResponse(1L, 1L, 2L, BigDecimal.valueOf(100.0), BigDecimal.valueOf(80.0), BigDecimal.valueOf(20.0), request.getBillingDate(), false);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.of(prescription));
        when(billingMapper.toEntity(request)).thenReturn(billing);
        when(billingRepository.save(billing)).thenReturn(saved);
        when(billingMapper.toResponse(saved)).thenReturn(response);

        BillingResponse result = billingService.create(request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(20.0), result.getDueAmount());
        assertFalse(result.getPaid());
        verify(billingRepository).save(billing);
    }

    @Test
    void shouldThrowWhenPatientNotFoundOnCreate() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(99L);
        request.setPrescriptionId(2L);
        request.setTotalAmount(BigDecimal.valueOf(100.0));
        request.setPaidAmount(BigDecimal.valueOf(80.0));
        request.setBillingDate(LocalDate.now());

        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> billingService.create(request));
        assertEquals("Patient not found", exception.getMessage());
        verify(billingRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenPrescriptionNotFound() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(1L);
        request.setPrescriptionId(2L);
        request.setTotalAmount(BigDecimal.valueOf(100.0));
        request.setPaidAmount(BigDecimal.valueOf(80.0));
        request.setBillingDate(LocalDate.now());

        when(patientRepository.findById(1L)).thenReturn(Optional.of(new Patient()));
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> billingService.create(request));
        assertEquals("Prescription not found", exception.getMessage());
        verify(billingRepository, never()).save(any());
    }

    @Test
    void shouldUpdateBilling() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(1L);
        request.setPrescriptionId(2L);
        request.setTotalAmount(BigDecimal.valueOf(200.0));
        request.setPaidAmount(BigDecimal.valueOf(200.0));
        request.setBillingDate(LocalDate.now());

        Patient patient = new Patient();
        patient.setId(1L);
        Prescription prescription = new Prescription();
        prescription.setId(2L);
        Billing billing = new Billing();
        billing.setId(1L);
        BillingResponse response = new BillingResponse(1L, 1L, 2L, BigDecimal.valueOf(200.0), BigDecimal.valueOf(200.0), BigDecimal.ZERO, request.getBillingDate(), true);

        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.of(prescription));
        when(billingRepository.save(billing)).thenReturn(billing);
        when(billingMapper.toResponse(billing)).thenReturn(response);

        BillingResponse result = billingService.update(1L, request);

        assertNotNull(result);
        assertTrue(result.getPaid());
        assertEquals(BigDecimal.ZERO, result.getDueAmount());
        verify(billingRepository).save(billing);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentBilling() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(1L);
        request.setPrescriptionId(2L);
        request.setTotalAmount(BigDecimal.valueOf(100.0));
        request.setPaidAmount(BigDecimal.valueOf(80.0));
        request.setBillingDate(LocalDate.now());

        when(billingRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> billingService.update(99L, request));
        assertEquals("Billing not found", exception.getMessage());
        verify(billingRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingBillingAndPatientNotFound() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(99L);
        request.setPrescriptionId(2L);
        request.setTotalAmount(BigDecimal.valueOf(100.0));
        request.setPaidAmount(BigDecimal.valueOf(80.0));
        request.setBillingDate(LocalDate.now());

        Billing billing = new Billing();
        billing.setId(1L);

        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> billingService.update(1L, request));
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUpdatingBillingAndPrescriptionNotFound() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(1L);
        request.setPrescriptionId(99L);
        request.setTotalAmount(BigDecimal.valueOf(100.0));
        request.setPaidAmount(BigDecimal.valueOf(80.0));
        request.setBillingDate(LocalDate.now());

        Patient patient = new Patient();
        patient.setId(1L);
        Billing billing = new Billing();
        billing.setId(1L);

        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(prescriptionRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> billingService.update(1L, request));
        assertEquals("Prescription not found", exception.getMessage());
    }

    @Test
    void shouldDeleteBilling() {
        billingService.delete(1L);

        verify(billingRepository).deleteById(1L);
    }

    @Test
    void shouldFindBillingById() {
        Billing billing = new Billing();
        billing.setId(1L);
        Patient patient = new Patient();
        patient.setId(1L);
        Prescription prescription = new Prescription();
        prescription.setId(2L);
        billing.setPatient(patient);
        billing.setPrescription(prescription);

        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(billingMapper.toResponse(billing)).thenReturn(new BillingResponse(1L, 1L, 2L, BigDecimal.valueOf(100.0), BigDecimal.valueOf(80.0), BigDecimal.valueOf(20.0), LocalDate.now(), false));

        BillingResponse result = billingService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenFindingNonExistentBilling() {
        when(billingRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> billingService.findById(99L));
        assertEquals("Billing not found", exception.getMessage());
    }

    @Test
    void shouldFindAllBillings() {
        Billing billing1 = new Billing();
        billing1.setId(1L);
        Billing billing2 = new Billing();
        billing2.setId(2L);

        when(billingRepository.findAll()).thenReturn(List.of(billing1, billing2));
        when(billingMapper.toResponse(billing1)).thenReturn(new BillingResponse(1L, 1L, 2L, BigDecimal.valueOf(100.0), BigDecimal.valueOf(80.0), BigDecimal.valueOf(20.0), LocalDate.now(), false));
        when(billingMapper.toResponse(billing2)).thenReturn(new BillingResponse(2L, 1L, 2L, BigDecimal.valueOf(200.0), BigDecimal.valueOf(200.0), BigDecimal.ZERO, LocalDate.now(), true));

        List<BillingResponse> result = billingService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoBillings() {
        when(billingRepository.findAll()).thenReturn(Collections.emptyList());

        List<BillingResponse> result = billingService.findAll();

        assertTrue(result.isEmpty());
    }
}