package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LabOrderRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long testId;

    @NotNull
    private Long orderedById; // doctor id

    private Long appointmentId;
    private String status; // PENDING, SAMPLE_COLLECTED, IN_PROGRESS, COMPLETED, CANCELLED
    private String resultValue;
    private String resultUrl;
    private String remarks;
}
