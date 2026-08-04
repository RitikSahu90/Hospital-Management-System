package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.entity.Billing;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BillingMapper {
    public Billing toEntity(BillingRequest request) {
        return Billing.builder()
                .consultationFee(request.getConsultationFee())
                .medicineCharges(request.getMedicineCharges())
                .otherCharges(request.getOtherCharges())
                .build();
    }

    public BillingResponse toResponse(Billing billing, BigDecimal paidAmount) {
        BigDecimal paid = paidAmount == null ? BigDecimal.ZERO : paidAmount;
        BigDecimal total = billing.getTotalAmount();
        if (total == null) {
            total = billing.getConsultationFee().add(billing.getMedicineCharges()).add(billing.getOtherCharges());
        }
        return new BillingResponse(
                billing.getId(),
                billing.getPatient().getId(),
                billing.getAppointment() == null ? null : billing.getAppointment().getId(),
                billing.getConsultationFee(), billing.getMedicineCharges(), billing.getOtherCharges(), total,
                paid, total.subtract(paid), billing.getStatus()
        );
    }
}
