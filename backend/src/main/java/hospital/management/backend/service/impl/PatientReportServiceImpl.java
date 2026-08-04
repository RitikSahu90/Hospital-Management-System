package hospital.management.backend.service.impl;

import hospital.management.backend.config.SecretsConfig;
import hospital.management.backend.dto.response.PatientReportResponse;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.PatientReport;
import hospital.management.backend.repository.PatientReportRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.service.PatientReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientReportServiceImpl implements PatientReportService {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of("application/pdf", "image/jpeg", "image/png");

    private final PatientRepository patientRepository;
    private final PatientReportRepository reportRepository;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final SecretsConfig secretsConfig;

    @Override
    public PatientReportResponse upload(Long patientId, String title, MultipartFile file) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("Report title is required");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Report file is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Report file must be 10 MB or smaller");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF, JPEG, and PNG reports are supported");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        String key = "patients/" + patientId + "/reports/" + UUID.randomUUID() + "-"
                + StringUtils.cleanPath(file.getOriginalFilename() == null ? "report" : file.getOriginalFilename());
        String bucket = secretsConfig.getAws().getS3Bucket();
        if (!StringUtils.hasText(bucket)) {
            throw new IllegalArgumentException("AWS S3 bucket is not configured");
        }

        s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key)
                .contentType(file.getContentType()).build(), RequestBody.fromBytes(readBytes(file)));
        try {
            PatientReport report = new PatientReport();
            report.setPatient(patient);
            report.setTitle(title.trim());
            report.setReportUrl(key);
            return toResponse(reportRepository.save(report));
        } catch (RuntimeException ex) {
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientReportResponse> findByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return reportRepository.findByPatient(patient).stream().map(this::toResponse).toList();
    }

        @Override
        @Transactional(readOnly = true)
    public String createDownloadUrl(Long patientId, Long reportId) {
        PatientReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found"));
        if (!report.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException("Report does not belong to this patient");
        }
        String bucket = secretsConfig.getAws().getS3Bucket();
        if (!StringUtils.hasText(bucket)) {
            throw new IllegalArgumentException("AWS S3 bucket is not configured");
        }
        GetObjectRequest objectRequest = GetObjectRequest.builder().bucket(bucket).key(report.getReportUrl()).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(java.time.Duration.ofMinutes(10)).getObjectRequest(objectRequest).build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private PatientReportResponse toResponse(PatientReport report) {
        return new PatientReportResponse(report.getId(), report.getPatient().getId(), report.getTitle(),
                report.getReportUrl(), report.getCreatedAt(), report.getUpdatedAt());
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to read report file", ex);
        }
    }
}
