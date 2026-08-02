package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordRequest {
    @NotNull private Long appointmentId;
    @NotBlank private String diagnosis;
    private String clinicalNotes;
}