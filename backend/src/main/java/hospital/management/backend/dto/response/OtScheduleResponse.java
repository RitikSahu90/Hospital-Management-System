package hospital.management.backend.dto.response;

import hospital.management.backend.enums.OtStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtScheduleResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long surgeonId;
    private String surgeonName;
    private Long theatreId;
    private String theatreName;
    private String surgeryType;
    private LocalDateTime scheduledAt;
    private Integer estimatedDuration;
    private OtStatus status;
    private String preOpNotes;
    private String surgeryNotes;
    private String postOpNotes;
    private String anaesthesiologist;
    private Instant createdAt;
    private Instant updatedAt;
}
