package hospital.management.backend.dto.response;

import hospital.management.backend.enums.AlertSeverity;
import hospital.management.backend.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAlertResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private AlertType alertType;
    private AlertSeverity severity;
    private String title;
    private String description;
    private String source;
    private Boolean isAcknowledged;
    private Long acknowledgedById;
    private String acknowledgedByName;
    private Instant acknowledgedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
