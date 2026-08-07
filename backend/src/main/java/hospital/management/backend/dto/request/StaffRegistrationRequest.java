package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StaffRegistrationRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String role; // "DOCTOR", "RECEPTIONIST", "PHARMACIST"
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;

    // Doctor specific fields (optional for others)
    private Long departmentId;
    private String doctorCode;
    private String licenseNumber;
    private String specialization;
    private String phone;
    private Double consultationFee;
    private Integer yearsExperience;
}
