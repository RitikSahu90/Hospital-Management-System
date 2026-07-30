package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;
import hospital.management.backend.entity.Prescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrescriptionMapper {
    public Prescription toEntity(PrescriptionRequest request) {
        return Prescription.builder()
                .medicineName(request.getMedicineName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .durationDays(request.getDurationDays())
                .prescribedDate(request.getPrescribedDate())
                .notes(request.getNotes())
                .build();
    }

    public PrescriptionResponse toResponse(Prescription prescription) {
        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getDoctor().getId(),
                prescription.getMedicineName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescription.getDurationDays(),
                prescription.getPrescribedDate(),
                prescription.getNotes()
        );
    }
}
