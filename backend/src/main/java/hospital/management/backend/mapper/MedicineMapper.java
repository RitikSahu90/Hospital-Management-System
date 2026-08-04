package hospital.management.backend.mapper;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {
    public Medicine toEntity(MedicineRequest request) {
        return Medicine.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .unitPrice(request.getUnitPrice())
                .build();
    }

    public MedicineResponse toResponse(Medicine medicine) {
        return toResponse(medicine, null);
    }

    public MedicineResponse toResponse(Medicine medicine, Inventory inventory) {
        return new MedicineResponse(
                medicine.getId(),
                medicine.getSupplier() == null ? null : medicine.getSupplier().getId(),
                medicine.getName(),
                medicine.getManufacturer(),
                medicine.getUnitPrice(),
                inventory == null ? null : inventory.getStockQuantity(),
                inventory == null ? null : inventory.getReorderLevel(),
                inventory == null ? null : inventory.getExpiryDate()
        );
    }
}
