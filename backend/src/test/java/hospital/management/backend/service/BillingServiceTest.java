package hospital.management.backend.service;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.entity.Billing;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.mapper.BillingMapper;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PaymentRepository;
import hospital.management.backend.service.impl.BillingServiceImpl;
import hospital.management.backend.enums.BillingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import hospital.management.backend.notification.service.NotificationService;

class BillingServiceTest {
    @Mock private BillingRepository billingRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private BillingMapper billingMapper;
    @Mock private NotificationService notificationService;
    @InjectMocks private BillingServiceImpl billingService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    private BillingRequest request() {
        BillingRequest request = new BillingRequest();
        request.setPatientId(1L);
        request.setConsultationFee(new BigDecimal("100.00"));
        request.setMedicineCharges(new BigDecimal("25.00"));
        request.setOtherCharges(new BigDecimal("5.00"));
        return request;
    }

    @Test void createsNormalizedBill() {
        Patient patient = new Patient(); patient.setId(1L);
        Billing billing = new Billing(); billing.setId(1L); billing.setPatient(patient);
        BillingResponse response = new BillingResponse(1L, 1L, null, new BigDecimal("100.00"), new BigDecimal("25.00"), new BigDecimal("5.00"), new BigDecimal("130.00"), BigDecimal.ZERO, new BigDecimal("130.00"), BillingStatus.PENDING);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(billingMapper.toEntity(any(BillingRequest.class))).thenReturn(billing);
        when(billingRepository.save(billing)).thenReturn(billing);
        when(paymentRepository.findByBillId(1L)).thenReturn(List.of());
        when(billingMapper.toResponse(billing, BigDecimal.ZERO)).thenReturn(response);

        BillingResponse result = billingService.create(request());

        assertEquals(new BigDecimal("130.00"), result.getTotalAmount());
        verify(billingRepository).save(billing);
    }

    @Test void rejectsMissingPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> billingService.create(request()));
        verify(billingRepository, never()).save(any());
    }

    @Test void aggregatesPaymentsForRead() {
        Patient patient = new Patient(); patient.setId(1L);
        Billing billing = new Billing(); billing.setId(1L); billing.setPatient(patient);
        BillingResponse response = new BillingResponse(1L, 1L, null, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("4.00"), new BigDecimal("6.00"), BillingStatus.PARTIALLY_PAID);
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(paymentRepository.findByBillId(1L)).thenReturn(List.of());
        when(billingMapper.toResponse(billing, BigDecimal.ZERO)).thenReturn(response);
        assertEquals(new BigDecimal("6.00"), billingService.findById(1L).getDueAmount());
    }

    @Test void deletesExistingBill() {
        when(billingRepository.existsById(1L)).thenReturn(true);
        billingService.delete(1L);
        verify(billingRepository).deleteById(1L);
    }
}
