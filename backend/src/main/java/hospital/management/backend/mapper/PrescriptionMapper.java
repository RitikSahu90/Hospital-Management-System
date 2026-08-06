package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.PrescriptionRequest;
import hospital.management.backend.dto.response.PrescriptionResponse;
import hospital.management.backend.dto.response.PrescriptionItemResponse;
import hospital.management.backend.entity.Prescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrescriptionMapper {
    public Prescription toEntity(PrescriptionRequest request) {
        return Prescription.builder()
                .notes(request.getNotes())
                .build();
    }

    public PrescriptionResponse toResponse(Prescription prescription) {
        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getDoctor().getId(),
                prescription.getMedicalRecord().getId(),
                prescription.getItems().stream().map(item -> new PrescriptionItemResponse(
                    item.getId(), item.getMedicine().getId(), item.getDosage(), item.getDurationDays(), item.getQuantity())).toList(),
                prescription.getNotes(),
                prescription.getPdfUrl()
        );
    }
}
