package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.mapper.PrescriptionMapper;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
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

    @Override
    public PrescriptionResponse create(PrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Prescription prescription = prescriptionMapper.toEntity(request);
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);

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
        prescription.setMedicineName(request.getMedicineName());
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setDurationDays(request.getDurationDays());
        prescription.setPrescribedDate(request.getPrescribedDate());
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
}
