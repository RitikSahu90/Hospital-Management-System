package hospital.management.backend.dto.response;

import hospital.management.backend.enums.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private Long userId;
    private Long departmentId;
    private String doctorCode;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String specialization;
    private String phone;
    private Integer yearsExperience;
    private Double consultationFee;
    private DoctorStatus status;
}