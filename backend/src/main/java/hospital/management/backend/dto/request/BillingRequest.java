package hospital.management.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillingRequest {
    @NotNull
    private Long patientId;

    private Long appointmentId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal consultationFee;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal medicineCharges;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal otherCharges;
}
