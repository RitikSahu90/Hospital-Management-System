package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OpdIpdVisitRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    private Long appointmentId;

    @NotNull
    private String visitType; // OPD or IPD

    @NotNull
    private String chiefComplaint;

    private String ward;
    private String bedNumber;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String status; // ACTIVE, COMPLETED, REFERRED, AMA, etc.
    private String notes;
}
