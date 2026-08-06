package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.EmergencyCaseRequest;
import hospital.management.backend.dto.response.EmergencyCaseResponse;
import hospital.management.backend.entity.EmergencyCase;
import hospital.management.backend.enums.EmergencyStatus;
import hospital.management.backend.enums.TriageLevel;
import org.springframework.stereotype.Component;

@Component
public class EmergencyCaseMapper {

    public EmergencyCaseResponse toResponse(EmergencyCase emergencyCase) {
        if (emergencyCase == null) return null;
        return new EmergencyCaseResponse(
                emergencyCase.getId(),
                emergencyCase.getPatient() != null ? emergencyCase.getPatient().getId() : null,
                emergencyCase.getPatient() != null ? (emergencyCase.getPatient().getFirstName() + " " + emergencyCase.getPatient().getLastName()) : "Unidentified Patient",
                emergencyCase.getTriageLevel(),
                emergencyCase.getChiefComplaint(),
                emergencyCase.getArrivalTime(),
                emergencyCase.getAssignedDoctor() != null ? emergencyCase.getAssignedDoctor().getId() : null,
                emergencyCase.getAssignedDoctor() != null ? ("Dr. " + emergencyCase.getAssignedDoctor().getFirstName() + " " + emergencyCase.getAssignedDoctor().getLastName()) : "Unassigned",
                emergencyCase.getStatus(),
                emergencyCase.getResolutionNotes(),
                emergencyCase.getResolvedAt(),
                emergencyCase.getCreatedAt(),
                emergencyCase.getUpdatedAt()
        );
    }

    public EmergencyCase toEntity(EmergencyCaseRequest request) {
        if (request == null) return null;
        return EmergencyCase.builder()
                .triageLevel(TriageLevel.valueOf(request.getTriageLevel().toUpperCase()))
                .chiefComplaint(request.getChiefComplaint())
                .status(request.getStatus() != null ? EmergencyStatus.valueOf(request.getStatus().toUpperCase()) : EmergencyStatus.WAITING)
                .resolutionNotes(request.getResolutionNotes())
                .build();
    }
}
