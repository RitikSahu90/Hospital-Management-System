package hospital.management.backend.fhir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFhirResource {
    private Long id;
    private Reference patient;
    private Reference doctor;
    private String status;
    private String start;
    private String end;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reference {
        private String reference;
        private String display;
    }
}
