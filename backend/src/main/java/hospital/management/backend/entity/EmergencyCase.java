package hospital.management.backend.entity;

import hospital.management.backend.enums.TriageLevel;
import hospital.management.backend.enums.EmergencyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "emergency_cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;              // nullable for unidentified patients

    @Enumerated(EnumType.STRING)
    @Column(name = "triage_level", nullable = false)
    @Builder.Default
    private TriageLevel triageLevel = TriageLevel.P3;

    @Column(name = "chief_complaint", nullable = false, length = 500)
    private String chiefComplaint;

    @Column(name = "arrival_time", nullable = false)
    private Instant arrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_doctor_id")
    private Doctor assignedDoctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EmergencyStatus status = EmergencyStatus.WAITING;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (arrivalTime == null) arrivalTime = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
