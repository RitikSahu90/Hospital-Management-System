package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {
    public Medicine toEntity(MedicineRequest request) {
        return Medicine.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .unitPrice(request.getUnitPrice())
                .stockQuantity(request.getStockQuantity())
                .expiryDate(request.getExpiryDate())
                .build();
    }

    public MedicineResponse toResponse(Medicine medicine) {
        return new MedicineResponse(
                medicine.getId(),
                medicine.getName(),
                medicine.getManufacturer(),
                medicine.getUnitPrice(),
                medicine.getStockQuantity(),
                medicine.getExpiryDate()
        );
    }
}
