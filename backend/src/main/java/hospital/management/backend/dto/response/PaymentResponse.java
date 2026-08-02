package hospital.management.backend.dto.response;

import hospital.management.backend.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long billId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private Instant paidAt;
}