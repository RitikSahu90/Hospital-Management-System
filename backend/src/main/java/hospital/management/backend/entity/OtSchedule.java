package hospital.management.backend.entity;

import hospital.management.backend.enums.OtStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "ot_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "surgeon_id", nullable = false)
    private Doctor surgeon;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private OperationTheatre theatre;

    @Column(name = "surgery_type", nullable = false, length = 200)
    private String surgeryType;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;      // minutes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OtStatus status = OtStatus.SCHEDULED;

    @Column(name = "pre_op_notes", columnDefinition = "TEXT")
    private String preOpNotes;

    @Column(name = "surgery_notes", columnDefinition = "TEXT")
    private String surgeryNotes;

    @Column(name = "post_op_notes", columnDefinition = "TEXT")
    private String postOpNotes;

    @Column(name = "anaesthesiologist", length = 150)
    private String anaesthesiologist;

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
