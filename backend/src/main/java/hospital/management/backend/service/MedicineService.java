package hospital.management.backend.service;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;

import java.util.List;

public interface MedicineService {
    MedicineResponse create(MedicineRequest request);
    MedicineResponse update(Long id, MedicineRequest request);
    void delete(Long id);
    MedicineResponse findById(Long id);
    List<MedicineResponse> findAll();
    MedicineResponse reduceStock(Long id, Integer quantity);
    MedicineResponse increaseStock(Long id, Integer quantity);
}
