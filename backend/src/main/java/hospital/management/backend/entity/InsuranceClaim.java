package hospital.management.backend.entity;

import hospital.management.backend.enums.InsuranceClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "insurance_claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Billing bill;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private InsuranceProvider provider;

    @Column(name = "claim_number", nullable = false, unique = true, length = 100)
    private String claimNumber;

    @Column(name = "policy_number", length = 100)
    private String policyNumber;

    @Column(name = "amount_claimed", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountClaimed;

    @Column(name = "amount_approved", precision = 12, scale = 2)
    private BigDecimal amountApproved;

    @Column(name = "amount_settled", precision = 12, scale = 2)
    private BigDecimal amountSettled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private InsuranceClaimStatus status = InsuranceClaimStatus.DRAFT;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "settled_at")
    private Instant settledAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
