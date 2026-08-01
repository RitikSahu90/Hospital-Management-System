package hospital.management.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "prescription_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrescriptionItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "prescription_id", nullable = false) private Prescription prescription;
    @ManyToOne(optional = false) @JoinColumn(name = "medicine_id", nullable = false) private Medicine medicine;
    @Column(nullable = false, length = 100) private String dosage;
    @Column(name = "duration_days", nullable = false) private Integer durationDays;
    @Column(nullable = false) private Integer quantity;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    @PrePersist void onCreate() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }
}