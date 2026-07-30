package hospital.management.backend.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PrescriptionRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotBlank
    private String medicineName;

    @NotBlank
    private String dosage;

    @NotBlank
    private String frequency;

    @NotNull
    @Min(1)
    private Integer durationDays;

    @NotNull
    @FutureOrPresent
    private LocalDate prescribedDate;

    private String notes;
}
