package hospital.management.backend.dto.request;

import hospital.management.backend.enums.DepartmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentRequest {
    @NotBlank private String name;
    @NotBlank private String code;
    private String description;
    @NotNull private DepartmentStatus status = DepartmentStatus.ACTIVE;
}