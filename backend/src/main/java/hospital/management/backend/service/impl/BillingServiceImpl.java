package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.entity.Billing;
import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.mapper.BillingMapper;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.PaymentRepository;
import hospital.management.backend.service.BillingService;
import hospital.management.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final BillingMapper billingMapper;
    private final NotificationService notificationService;

    @Override
    public BillingResponse create(BillingRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Billing billing = billingMapper.toEntity(request);
        billing.setPatient(patient);
        billing.setAppointment(findAppointment(request.getAppointmentId()));

        Billing saved = billingRepository.save(billing);

        // Save database notification for the patient
        BigDecimal total = request.getConsultationFee().add(request.getMedicineCharges()).add(request.getOtherCharges());
        String msg = String.format("A new bill has been generated for you (Invoice #%d). Total: ₹%,.2f. Details: Consultation Fee: ₹%,.2f, Medicines: ₹%,.2f.",
                saved.getId(), total, request.getConsultationFee(), request.getMedicineCharges());
        notificationService.createNotification(patient, "New Bill Generated", msg);

        return toResponse(saved);
    }

    @Override
    public BillingResponse update(Long id, BillingRequest request) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billing not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        billing.setPatient(patient);
        billing.setAppointment(findAppointment(request.getAppointmentId()));
        billing.setConsultationFee(request.getConsultationFee());
        billing.setMedicineCharges(request.getMedicineCharges());
        billing.setOtherCharges(request.getOtherCharges());

        Billing saved = billingRepository.save(billing);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!billingRepository.existsById(id)) throw new IllegalArgumentException("Billing not found");
        billingRepository.deleteById(id);
    }

    @Override
    public BillingResponse findById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billing not found"));
        return toResponse(billing);
    }

    @Override
    public List<BillingResponse> findAll() {
                return billingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<BillingResponse> findAllForPatient(String patientUsername) {
        Patient patient = patientRepository.findByUserUsername(patientUsername)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
        return billingRepository.findAll().stream()
                .filter(b -> b.getPatient() != null && b.getPatient().getId().equals(patient.getId()))
                .map(this::toResponse)
                .toList();
    }

        private Appointment findAppointment(Long id) {
                return id == null ? null : appointmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        }

        private BillingResponse toResponse(Billing billing) {
                BigDecimal paid = paymentRepository.findByBillId(billing.getId()).stream().map(payment -> payment.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
                return billingMapper.toResponse(billing, paid);
        }
}
