package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DoctorRequest {
    private Long userId;

    @NotNull
    private Long departmentId;

    @NotBlank
    private String doctorCode;

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

    @NotNull
    @PositiveOrZero
    private Integer yearsExperience = 0;

    @NotNull
    private hospital.management.backend.enums.DoctorStatus status = hospital.management.backend.enums.DoctorStatus.ACTIVE;
}
