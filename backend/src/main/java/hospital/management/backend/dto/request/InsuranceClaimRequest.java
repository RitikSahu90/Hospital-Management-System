package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InsuranceClaimRequest {
    @NotNull
    private Long billId;

    @NotNull
    private Long patientId;

    @NotNull
    private Long providerId;

    private String policyNumber;

    @NotNull
    private BigDecimal amountClaimed;

    private BigDecimal amountApproved;
    private BigDecimal amountSettled;
    private String status; // DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, SETTLED, CANCELLED
    private String rejectionReason;
    private String notes;
}
