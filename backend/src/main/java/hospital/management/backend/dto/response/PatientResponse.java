package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String patientNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private java.time.LocalDate dateOfBirth;
    private hospital.management.backend.enums.Gender gender;
    private String address;
    private String bloodGroup;
    private String diagnosis;
}
