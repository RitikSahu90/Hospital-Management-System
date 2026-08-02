package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrescriptionItemResponse {
    private Long id;
    private Long medicineId;
    private String dosage;
    private Integer durationDays;
    private Integer quantity;
}