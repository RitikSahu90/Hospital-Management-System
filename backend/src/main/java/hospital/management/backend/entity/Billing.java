package hospital.management.backend.entity;

import hospital.management.backend.enums.BillingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "consultation_fee", nullable = false)
    @Builder.Default private BigDecimal consultationFee = BigDecimal.ZERO;

    @Column(name = "medicine_charges", nullable = false)
    @Builder.Default private BigDecimal medicineCharges = BigDecimal.ZERO;

    @Column(name = "other_charges", nullable = false)
    @Builder.Default private BigDecimal otherCharges = BigDecimal.ZERO;

    @org.hibernate.annotations.Formula("consultation_fee + medicine_charges + other_charges")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default private BillingStatus status = BillingStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
