package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.OtScheduleRequest;
import hospital.management.backend.dto.response.OperationTheatreResponse;
import hospital.management.backend.dto.response.OtScheduleResponse;
import hospital.management.backend.entity.OperationTheatre;
import hospital.management.backend.entity.OtSchedule;
import hospital.management.backend.enums.OtStatus;
import org.springframework.stereotype.Component;

@Component
public class OtMapper {

    public OperationTheatreResponse toResponse(OperationTheatre theatre) {
        if (theatre == null) return null;
        return new OperationTheatreResponse(
                theatre.getId(),
                theatre.getName(),
                theatre.getFloor(),
                theatre.getStatus(),
                theatre.getCreatedAt(),
                theatre.getUpdatedAt()
        );
    }

    public OtScheduleResponse toResponse(OtSchedule schedule) {
        if (schedule == null) return null;
        return new OtScheduleResponse(
                schedule.getId(),
                schedule.getPatient().getId(),
                schedule.getPatient().getFirstName() + " " + schedule.getPatient().getLastName(),
                schedule.getSurgeon().getId(),
                "Dr. " + schedule.getSurgeon().getFirstName() + " " + schedule.getSurgeon().getLastName(),
                schedule.getTheatre().getId(),
                schedule.getTheatre().getName(),
                schedule.getSurgeryType(),
                schedule.getScheduledAt(),
                schedule.getEstimatedDuration(),
                schedule.getStatus(),
                schedule.getPreOpNotes(),
                schedule.getSurgeryNotes(),
                schedule.getPostOpNotes(),
                schedule.getAnaesthesiologist(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    public OtSchedule toEntity(OtScheduleRequest request) {
        if (request == null) return null;
        return OtSchedule.builder()
                .surgeryType(request.getSurgeryType())
                .scheduledAt(request.getScheduledAt())
                .estimatedDuration(request.getEstimatedDuration())
                .status(request.getStatus() != null ? OtStatus.valueOf(request.getStatus().toUpperCase()) : OtStatus.SCHEDULED)
                .preOpNotes(request.getPreOpNotes())
                .surgeryNotes(request.getSurgeryNotes())
                .postOpNotes(request.getPostOpNotes())
                .anaesthesiologist(request.getAnaesthesiologist())
                .build();
    }
}
