package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PatientReportResponse {
    private Long id;
    private Long patientId;
    private String title;
    private String reportUrl;
    private Instant createdAt;
    private Instant updatedAt;
}