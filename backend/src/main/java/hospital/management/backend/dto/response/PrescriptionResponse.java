package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@lombok.NoArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long medicalRecordId;
    private List<PrescriptionItemResponse> items;
    private String notes;
    private String pdfUrl;
}
