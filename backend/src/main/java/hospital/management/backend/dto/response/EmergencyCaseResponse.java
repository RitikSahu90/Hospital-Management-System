package hospital.management.backend.dto.response;

import hospital.management.backend.enums.EmergencyStatus;
import hospital.management.backend.enums.TriageLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCaseResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private TriageLevel triageLevel;
    private String chiefComplaint;
    private Instant arrivalTime;
    private Long assignedDoctorId;
    private String assignedDoctorName;
    private EmergencyStatus status;
    private String resolutionNotes;
    private Instant resolvedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
