package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.mapper.MedicineMapper;
import hospital.management.backend.repository.MedicineRepository;
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
    private final MedicineMapper medicineMapper;

    @Override
    public MedicineResponse create(MedicineRequest request) {
        Medicine medicine = medicineMapper.toEntity(request);
        Medicine saved = medicineRepository.save(medicine);
        return medicineMapper.toResponse(saved);
    }

    @Override
    public MedicineResponse update(Long id, MedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        medicine.setName(request.getName());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUnitPrice(request.getUnitPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setExpiryDate(request.getExpiryDate());
        Medicine saved = medicineRepository.save(medicine);
        return medicineMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public MedicineResponse findById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        return medicineMapper.toResponse(medicine);
    }

    @Override
    public List<MedicineResponse> findAll() {
        return medicineRepository.findAll().stream()
                .map(medicineMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicineResponse reduceStock(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        if (medicine.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
        return medicineMapper.toResponse(medicineRepository.save(medicine));
    }

    @Override
    public MedicineResponse increaseStock(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));
        medicine.setStockQuantity(medicine.getStockQuantity() + quantity);
        return medicineMapper.toResponse(medicineRepository.save(medicine));
    }
}
