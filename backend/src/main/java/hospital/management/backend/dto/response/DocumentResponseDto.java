package hospital.management.backend.dto.response;

import java.time.Instant;

public record DocumentResponseDto(
    Long id,
    Long patientId,
    String patientName,
    String patientPhone,
    String fileType,
    String documentName,
    String s3Key,
    String downloadUrl,
    String uploadedBy,
    Instant timestamp
) {}
