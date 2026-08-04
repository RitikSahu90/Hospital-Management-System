package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RiskAlertRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private String alertType; // SEPSIS_RISK, EMERGENCY_PRIORITY, VITALS_CRITICAL, MEDICATION_ALERT, FALL_RISK, READMISSION_RISK, OTHER

    @NotNull
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @NotNull
    private String title;

    @NotNull
    private String description;

    private String source;
}
