package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.request.PrescriptionItemRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.entity.PrescriptionItem;
import hospital.management.backend.mapper.PrescriptionMapper;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import hospital.management.backend.repository.MedicalRecordRepository;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PrescriptionMapper prescriptionMapper;
        private final MedicalRecordRepository medicalRecordRepository;
        private final MedicineRepository medicineRepository;

    @Override
    public PrescriptionResponse create(PrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Prescription prescription = prescriptionMapper.toEntity(request);
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setMedicalRecord(medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found")));
        setItems(prescription, request);

        Prescription saved = prescriptionRepository.save(prescription);
        return prescriptionMapper.toResponse(saved);
    }

    @Override
    public PrescriptionResponse update(Long id, PrescriptionRequest request) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setMedicalRecord(medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new IllegalArgumentException("Medical record not found")));
        prescription.getItems().clear();
        setItems(prescription, request);
        prescription.setNotes(request.getNotes());

        Prescription saved = prescriptionRepository.save(prescription);
        return prescriptionMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        prescriptionRepository.deleteById(id);
    }

    @Override
    public PrescriptionResponse findById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));
        return prescriptionMapper.toResponse(prescription);
    }

    @Override
    public List<PrescriptionResponse> findAll() {
        return prescriptionRepository.findAll().stream()
                .map(prescriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

        private void setItems(Prescription prescription, PrescriptionRequest request) {
                for (PrescriptionItemRequest itemRequest : request.getItems()) {
                        PrescriptionItem item = new PrescriptionItem();
                        item.setPrescription(prescription);
                        item.setMedicine(medicineRepository.findById(itemRequest.getMedicineId())
                                        .orElseThrow(() -> new IllegalArgumentException("Medicine not found")));
                        item.setDosage(itemRequest.getDosage());
                        item.setDurationDays(itemRequest.getDurationDays());
                        item.setQuantity(itemRequest.getQuantity());
                        prescription.getItems().add(item);
                }
        }
}
