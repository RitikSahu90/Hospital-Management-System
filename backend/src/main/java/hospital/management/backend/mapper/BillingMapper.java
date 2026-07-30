package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.entity.Billing;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {
    public Billing toEntity(BillingRequest request) {
        return Billing.builder()
                .totalAmount(request.getTotalAmount())
                .paidAmount(request.getPaidAmount())
                .dueAmount(request.getTotalAmount().subtract(request.getPaidAmount()))
                .billingDate(request.getBillingDate())
                .paid(request.getPaidAmount().compareTo(request.getTotalAmount()) >= 0)
                .build();
    }

    public BillingResponse toResponse(Billing billing) {
        return new BillingResponse(
                billing.getId(),
                billing.getPatient().getId(),
                billing.getPrescription().getId(),
                billing.getTotalAmount(),
                billing.getPaidAmount(),
                billing.getDueAmount(),
                billing.getBillingDate(),
                billing.getPaid()
        );
    }
}
