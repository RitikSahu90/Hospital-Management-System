package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.OpdIpdVisitRequest;
import hospital.management.backend.dto.response.OpdIpdVisitResponse;
import hospital.management.backend.entity.OpdIpdVisit;
import hospital.management.backend.enums.VisitStatus;
import hospital.management.backend.enums.VisitType;
import org.springframework.stereotype.Component;

@Component
public class OpdIpdVisitMapper {

    public OpdIpdVisitResponse toResponse(OpdIpdVisit visit) {
        if (visit == null) return null;
        return new OpdIpdVisitResponse(
                visit.getId(),
                visit.getPatient().getId(),
                visit.getPatient().getFirstName() + " " + visit.getPatient().getLastName(),
                visit.getDoctor().getId(),
                "Dr. " + visit.getDoctor().getFirstName() + " " + visit.getDoctor().getLastName(),
                visit.getAppointment() != null ? visit.getAppointment().getId() : null,
                visit.getVisitType(),
                visit.getChiefComplaint(),
                visit.getWard(),
                visit.getBedNumber(),
                visit.getAdmissionDate(),
                visit.getDischargeDate(),
                visit.getStatus(),
                visit.getNotes(),
                visit.getCreatedAt(),
                visit.getUpdatedAt()
        );
    }

    public OpdIpdVisit toEntity(OpdIpdVisitRequest request) {
        if (request == null) return null;
        return OpdIpdVisit.builder()
                .visitType(VisitType.valueOf(request.getVisitType().toUpperCase()))
                .chiefComplaint(request.getChiefComplaint())
                .ward(request.getWard())
                .bedNumber(request.getBedNumber())
                .admissionDate(request.getAdmissionDate())
                .dischargeDate(request.getDischargeDate())
                .status(request.getStatus() != null ? VisitStatus.valueOf(request.getStatus().toUpperCase()) : VisitStatus.ACTIVE)
                .notes(request.getNotes())
                .build();
    }
}
