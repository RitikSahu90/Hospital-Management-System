package hospital.management.backend.dto.response;

import hospital.management.backend.enums.LabOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long testId;
    private String testName;
    private String testCategory;
    private Long orderedById;
    private String orderedByName;
    private Long appointmentId;
    private String orderNumber;
    private LabOrderStatus status;
    private String resultValue;
    private String resultUrl;
    private String remarks;
    private Instant orderedAt;
    private Instant completedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
