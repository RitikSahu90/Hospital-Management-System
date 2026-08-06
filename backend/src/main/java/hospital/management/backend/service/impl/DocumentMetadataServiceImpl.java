package hospital.management.backend.service.impl;

import hospital.management.backend.config.SecretsConfig;
import hospital.management.backend.dto.response.DocumentResponseDto;
import hospital.management.backend.entity.DocumentMetadata;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.repository.DocumentMetadataRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.service.DocumentMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentMetadataServiceImpl implements DocumentMetadataService {
    private final PatientRepository patientRepository;
    private final DocumentMetadataRepository documentMetadataRepository;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final SecretsConfig secretsConfig;

    @Override
    public DocumentResponseDto upload(Long patientId, String fileType, String documentName, MultipartFile file, String uploadedBy) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        String bucket = secretsConfig.getAws().getS3Bucket();
        if (!StringUtils.hasText(bucket)) {
            throw new IllegalArgumentException("AWS S3 bucket is not configured");
        }

        String originalName = file.getOriginalFilename() == null ? "document.pdf" : file.getOriginalFilename();
        String folder = fileType.toLowerCase() + "s";
        String key = folder + "/" + patientId + "/" + UUID.randomUUID() + "-" + StringUtils.cleanPath(originalName);

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build(), RequestBody.fromBytes(file.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("S3 Upload failed", ex);
        }

        DocumentMetadata metadata = DocumentMetadata.builder()
                .patient(patient)
                .fileType(fileType.toUpperCase())
                .documentName(documentName)
                .s3Key(key)
                .uploadedBy(uploadedBy)
                .build();

        DocumentMetadata saved = documentMetadataRepository.save(metadata);
        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponseDto> findAllForPatient(String patientUsername) {
        Patient patient = patientRepository.findByUserUsername(patientUsername)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
        return documentMetadataRepository.findByPatient(patient).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponseDto> findByPatientAndType(String patientUsername, String fileType) {
        Patient patient = patientRepository.findByUserUsername(patientUsername)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
        return documentMetadataRepository.findByPatientAndFileType(patient, fileType.toUpperCase()).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private DocumentResponseDto toResponseDto(DocumentMetadata metadata) {
        String downloadUrl = generatePresignedUrl(metadata.getS3Key());
        return new DocumentResponseDto(
                metadata.getId(),
                metadata.getPatient().getId(),
                metadata.getPatient().getFirstName() + " " + metadata.getPatient().getLastName(),
                metadata.getPatient().getPhone(),
                metadata.getFileType(),
                metadata.getDocumentName(),
                metadata.getS3Key(),
                downloadUrl,
                metadata.getUploadedBy(),
                metadata.getCreatedAt()
        );
    }

    private String generatePresignedUrl(String s3Key) {
        String bucket = secretsConfig.getAws().getS3Bucket();
        if (!StringUtils.hasText(bucket)) {
            return s3Key;
        }
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(s3Key).build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(java.time.Duration.ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build();
            return s3Presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception ex) {
            return s3Key;
        }
    }
}
