package hospital.management.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    @NotBlank
    private String status;
}
