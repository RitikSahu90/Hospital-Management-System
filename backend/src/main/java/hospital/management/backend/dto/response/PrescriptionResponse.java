package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private LocalDate prescribedDate;
    private String notes;
}
