package hospital.management.backend.dto.response;

import hospital.management.backend.enums.TheatreStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationTheatreResponse {
    private Long id;
    private String name;
    private String floor;
    private TheatreStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
