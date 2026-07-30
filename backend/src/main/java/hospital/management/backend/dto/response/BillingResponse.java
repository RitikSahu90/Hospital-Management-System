package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BillingResponse {
    private Long id;
    private Long patientId;
    private Long prescriptionId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private LocalDate billingDate;
    private Boolean paid;
}
