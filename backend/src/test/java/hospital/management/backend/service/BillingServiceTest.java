package hospital.management.backend.service;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.entity.Billing;
import hospital.management.backend.entity.Doctor;
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
}
