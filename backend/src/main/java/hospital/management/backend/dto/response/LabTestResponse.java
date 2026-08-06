package hospital.management.backend.dto.response;

import hospital.management.backend.enums.LabCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestResponse {
    private Long id;
    private String name;
    private LabCategory category;
    private String description;
    private String normalRange;
    private String unit;
    private BigDecimal price;
    private Boolean isActive;
}
