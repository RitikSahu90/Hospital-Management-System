package hospital.management.backend.service;

import hospital.management.backend.dto.request.SupplierRequest;
import hospital.management.backend.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse create(SupplierRequest request);
    List<SupplierResponse> findAll();
    SupplierResponse findById(Long id);
    SupplierResponse update(Long id, SupplierRequest request);
    void delete(Long id);
}