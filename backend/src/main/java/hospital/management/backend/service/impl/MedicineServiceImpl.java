package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.entity.Inventory;
import hospital.management.backend.entity.Supplier;
import hospital.management.backend.mapper.MedicineMapper;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.repository.InventoryRepository;
import hospital.management.backend.repository.SupplierRepository;
import hospital.management.backend.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponse create(MedicineRequest request) {
        Medicine medicine = medicineMapper.toEntity(request);
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        medicine.setSupplier(supplier);
        Medicine saved = medicineRepository.save(medicine);
        Inventory inventory = Inventory.builder().medicine(saved).stockQuantity(request.getStockQuantity())
            .reorderLevel(request.getReorderLevel()).expiryDate(request.getExpiryDate()).build();
        return medicineMapper.toResponse(saved, inventoryRepository.save(inventory));
    }

    @Override
    public MedicineResponse update(Long id, MedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        medicine.setSupplier(supplier);
        medicine.setName(request.getName());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUnitPrice(request.getUnitPrice());
        Medicine saved = medicineRepository.save(medicine);
        Inventory inventory = inventoryRepository.findByMedicine(saved)
            .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        inventory.setStockQuantity(request.getStockQuantity());
        inventory.setReorderLevel(request.getReorderLevel());
        inventory.setExpiryDate(request.getExpiryDate());
        return medicineMapper.toResponse(saved, inventoryRepository.save(inventory));
    }

    @Override
    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public MedicineResponse findById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        return medicineMapper.toResponse(medicine, inventoryRepository.findByMedicine(medicine).orElse(null));
    }

    @Override
    public List<MedicineResponse> findAll() {
        return medicineRepository.findAll().stream()
            .map(medicine -> medicineMapper.toResponse(medicine, inventoryRepository.findByMedicine(medicine).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public MedicineResponse reduceStock(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        Inventory inventory = inventoryRepository.findByMedicine(medicine)
            .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        if (inventory.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        inventory.setStockQuantity(inventory.getStockQuantity() - quantity);
        return medicineMapper.toResponse(medicine, inventoryRepository.save(inventory));
    }

    @Override
    public MedicineResponse increaseStock(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        Inventory inventory = inventoryRepository.findByMedicine(medicine)
            .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        inventory.setStockQuantity(inventory.getStockQuantity() + quantity);
        return medicineMapper.toResponse(medicine, inventoryRepository.save(inventory));
    }
}
