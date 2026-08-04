package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OtScheduleRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long surgeonId;

    @NotNull
    private Long theatreId;

    @NotNull
    private String surgeryType;

    @NotNull
    private LocalDateTime scheduledAt;

    private Integer estimatedDuration; // in minutes
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, POSTPONED
    private String preOpNotes;
    private String surgeryNotes;
    private String postOpNotes;
    private String anaesthesiologist;
}
