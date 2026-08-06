package hospital.management.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PrescriptionItem> items = new ArrayList<>();

    private String notes;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
