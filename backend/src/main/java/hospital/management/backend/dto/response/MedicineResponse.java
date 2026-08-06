package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MedicineResponse {
    private Long id;
    private Long supplierId;
    private String name;
    private String manufacturer;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;

    public MedicineResponse(Long id, String name, String manufacturer, BigDecimal unitPrice,
                            Integer stockQuantity, LocalDate expiryDate) {
        this(id, null, name, manufacturer, unitPrice, stockQuantity, null, expiryDate);
    }
}
