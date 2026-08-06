package hospital.management.backend.dto.response;

import hospital.management.backend.enums.VisitStatus;
import hospital.management.backend.enums.VisitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpdIpdVisitResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long appointmentId;
    private VisitType visitType;
    private String chiefComplaint;
    private String ward;
    private String bedNumber;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private VisitStatus status;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
}
