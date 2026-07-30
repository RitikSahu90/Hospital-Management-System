package hospital.management.backend.fhir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PractitionerFhirResource {
    private Long id;
    private String name;
    private String specialization;
    private String qualification;
}
