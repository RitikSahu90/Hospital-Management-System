package hospital.management.backend.service;

import hospital.management.backend.dto.request.DepartmentRequest;
import hospital.management.backend.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse create(DepartmentRequest request);
    List<DepartmentResponse> findAll();
    DepartmentResponse findById(Long id);
    DepartmentResponse update(Long id, DepartmentRequest request);
    void delete(Long id);
}