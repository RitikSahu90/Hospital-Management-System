package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import hospital.management.backend.enums.BillingStatus;

@Data
@AllArgsConstructor
public class BillingResponse {
    private Long id;
    private Long patientId;
    private Long appointmentId;
    private BigDecimal consultationFee;
    private BigDecimal medicineCharges;
    private BigDecimal otherCharges;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private BillingStatus status;
}
