package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long medicalRecordId;

    @NotNull
    private List<@jakarta.validation.Valid PrescriptionItemRequest> items;

    private String notes;
}
