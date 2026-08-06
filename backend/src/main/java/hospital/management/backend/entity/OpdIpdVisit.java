package hospital.management.backend.entity;

import hospital.management.backend.enums.VisitType;
import hospital.management.backend.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "opd_ipd_visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpdIpdVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false)
    @Builder.Default
    private VisitType visitType = VisitType.OPD;

    @Column(name = "chief_complaint", nullable = false, length = 500)
    private String chiefComplaint;

    @Column(length = 100)
    private String ward;

    @Column(name = "bed_number", length = 20)
    private String bedNumber;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VisitStatus status = VisitStatus.ACTIVE;

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
