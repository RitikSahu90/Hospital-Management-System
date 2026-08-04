package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.RiskAlertRequest;
import hospital.management.backend.dto.response.RiskAlertResponse;
import hospital.management.backend.entity.RiskAlert;
import hospital.management.backend.enums.AlertSeverity;
import hospital.management.backend.enums.AlertType;
import org.springframework.stereotype.Component;

@Component
public class RiskAlertMapper {

    public RiskAlertResponse toResponse(RiskAlert alert) {
        if (alert == null) return null;
        return new RiskAlertResponse(
                alert.getId(),
                alert.getPatient().getId(),
                alert.getPatient().getFirstName() + " " + alert.getPatient().getLastName(),
                alert.getAlertType(),
                alert.getSeverity(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getSource(),
                alert.getIsAcknowledged(),
                alert.getAcknowledgedBy() != null ? alert.getAcknowledgedBy().getId() : null,
                alert.getAcknowledgedBy() != null ? alert.getAcknowledgedBy().getUsername() : null,
                alert.getAcknowledgedAt(),
                alert.getCreatedAt(),
                alert.getUpdatedAt()
        );
    }

    public RiskAlert toEntity(RiskAlertRequest request) {
        if (request == null) return null;
        return RiskAlert.builder()
                .alertType(AlertType.valueOf(request.getAlertType().toUpperCase()))
                .severity(AlertSeverity.valueOf(request.getSeverity().toUpperCase()))
                .title(request.getTitle())
                .description(request.getDescription())
                .source(request.getSource())
                .isAcknowledged(false)
                .build();
    }
}
