package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MedicineResponse {
    private Long id;
    private String name;
    private String manufacturer;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private LocalDate expiryDate;
}
