package hospital.management.backend.service.impl;

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
import hospital.management.backend.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final BillingMapper billingMapper;

    @Override
    public BillingResponse create(BillingRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));

        Billing billing = billingMapper.toEntity(request);
        billing.setPatient(patient);
        billing.setPrescription(prescription);

        Billing saved = billingRepository.save(billing);
        return billingMapper.toResponse(saved);
    }

    @Override
    public BillingResponse update(Long id, BillingRequest request) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billing not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));

        billing.setPatient(patient);
        billing.setPrescription(prescription);
        billing.setTotalAmount(request.getTotalAmount());
        billing.setPaidAmount(request.getPaidAmount());
        billing.setDueAmount(request.getTotalAmount().subtract(request.getPaidAmount()));
        billing.setBillingDate(request.getBillingDate());
        billing.setPaid(request.getPaidAmount().compareTo(request.getTotalAmount()) >= 0);

        Billing saved = billingRepository.save(billing);
        return billingMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        billingRepository.deleteById(id);
    }

    @Override
    public BillingResponse findById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billing not found"));
        return billingMapper.toResponse(billing);
    }

    @Override
    public List<BillingResponse> findAll() {
        return billingRepository.findAll().stream()
                .map(billingMapper::toResponse)
                .collect(Collectors.toList());
    }
}
