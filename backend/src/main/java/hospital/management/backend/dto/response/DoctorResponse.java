package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String specialization;
    private String phone;
    private Double consultationFee;
}
