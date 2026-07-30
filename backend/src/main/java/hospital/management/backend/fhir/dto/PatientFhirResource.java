package hospital.management.backend.fhir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientFhirResource {
    private Long id;
    private String name;
    private String gender;
    private String birthDate;
    private List<String> telecom;
    private List<String> address;
}
