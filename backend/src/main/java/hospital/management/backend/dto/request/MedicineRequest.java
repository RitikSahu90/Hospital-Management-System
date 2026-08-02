package hospital.management.backend.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicineRequest {
    @NotNull
    private Long supplierId;

    @NotBlank
    private String name;

    @NotBlank
    private String manufacturer;

    @NotNull
    @Min(0)
    private BigDecimal unitPrice;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @NotNull
    @Min(0)
    private Integer reorderLevel = 10;

    @NotNull
    @Future
    private LocalDate expiryDate;
}
