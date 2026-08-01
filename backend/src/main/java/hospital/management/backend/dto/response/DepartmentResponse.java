package hospital.management.backend.dto.response;

import hospital.management.backend.enums.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private DepartmentStatus status;
}