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
import hospital.management.backend.config.SecretsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.util.List;
import java.util.UUID;
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
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final SecretsConfig secretsConfig;

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

    @Override
    public List<PrescriptionResponse> findAllForPatient(String patientUsername) {
        Patient patient = patientRepository.findByUserUsername(patientUsername)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
        return prescriptionRepository.findAll().stream()
                .filter(p -> p.getPatient() != null && p.getPatient().getId().equals(patient.getId()))
                .map(prescriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponse uploadPdf(Long id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF prescription files are supported");
        }
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));

        String bucket = secretsConfig.getAws().getS3Bucket();
        if (!StringUtils.hasText(bucket)) {
            throw new IllegalArgumentException("AWS S3 bucket is not configured");
        }

        String key = "prescriptions/" + id + "/" + UUID.randomUUID() + "-"
                + StringUtils.cleanPath(file.getOriginalFilename() == null ? "prescription.pdf" : file.getOriginalFilename());

        s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key)
                .contentType(file.getContentType()).build(), RequestBody.fromBytes(readBytes(file)));

        prescription.setPdfUrl(key);
        Prescription saved = prescriptionRepository.save(prescription);
        return prescriptionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public String createDownloadUrl(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));
        if (!StringUtils.hasText(prescription.getPdfUrl())) {
            throw new IllegalArgumentException("No PDF uploaded for this prescription");
        }

        String bucket = secretsConfig.getAws().getS3Bucket();
        if (!StringUtils.hasText(bucket)) {
            throw new IllegalArgumentException("AWS S3 bucket is not configured");
        }

        GetObjectRequest objectRequest = GetObjectRequest.builder().bucket(bucket).key(prescription.getPdfUrl()).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(java.time.Duration.ofMinutes(10)).getObjectRequest(objectRequest).build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException("Failed to read file bytes", e);
        }
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
