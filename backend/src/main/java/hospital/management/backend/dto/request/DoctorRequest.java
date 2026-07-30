package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DoctorRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String specialization;

    private String phone;

    @PositiveOrZero
    private Double consultationFee;
}
