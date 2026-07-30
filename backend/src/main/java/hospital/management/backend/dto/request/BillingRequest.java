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

    @NotNull
    private Long prescriptionId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal paidAmount;

    @NotNull
    private LocalDate billingDate;
}
