package hospital.management.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionItemRequest {
    @NotNull
    private Long medicineId;
    @NotBlank
    private String dosage;
    @NotNull
    @Min(1)
    private Integer durationDays;
    @NotNull
    @Min(1)
    private Integer quantity;
}