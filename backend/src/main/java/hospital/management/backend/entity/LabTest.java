package hospital.management.backend.entity;

import hospital.management.backend.enums.LabCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "lab_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LabCategory category = LabCategory.OTHER;

    @Column(length = 500)
    private String description;

    @Column(name = "normal_range", length = 100)
    private String normalRange;

    @Column(length = 50)
    private String unit;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

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
