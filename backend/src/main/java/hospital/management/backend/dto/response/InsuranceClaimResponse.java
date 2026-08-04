package hospital.management.backend.dto.response;

import hospital.management.backend.enums.InsuranceClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaimResponse {
    private Long id;
    private Long billId;
    private Long patientId;
    private String patientName;
    private Long providerId;
    private String providerName;
    private String claimNumber;
    private String policyNumber;
    private BigDecimal amountClaimed;
    private BigDecimal amountApproved;
    private BigDecimal amountSettled;
    private InsuranceClaimStatus status;
    private String rejectionReason;
    private Instant submittedAt;
    private Instant approvedAt;
    private Instant settledAt;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
}
