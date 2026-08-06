package hospital.management.backend.entity;

import hospital.management.backend.enums.TheatreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "operation_theatres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationTheatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TheatreStatus status = TheatreStatus.AVAILABLE;

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
