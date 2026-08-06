package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmergencyCaseRequest {
    private Long patientId; // can be null for unidentified patients

    @NotNull
    private String triageLevel; // P1, P2, P3, P4

    @NotNull
    private String chiefComplaint;

    private Long assignedDoctorId;
    private String status; // WAITING, ACTIVE, ADMITTED, DISCHARGED, TRANSFERRED, DECEASED
    private String resolutionNotes;
}
