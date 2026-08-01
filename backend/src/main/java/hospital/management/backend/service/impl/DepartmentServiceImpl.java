package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.DepartmentRequest;
import hospital.management.backend.dto.response.DepartmentResponse;
import hospital.management.backend.entity.Department;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    public DepartmentResponse create(DepartmentRequest request) {
        Department department = new Department();
        apply(department, request);
        return toResponse(repository.save(department));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() { return repository.findAll().stream().map(this::toResponse).toList(); }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) { return toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Department not found"))); }

    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Department not found"));
        apply(department, request);
        return toResponse(repository.save(department));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Department not found");
        repository.deleteById(id);
    }

    private void apply(Department department, DepartmentRequest request) {
        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());
        department.setStatus(request.getStatus());
    }

    private DepartmentResponse toResponse(Department department) { return new DepartmentResponse(department.getId(), department.getName(), department.getCode(), department.getDescription(), department.getStatus()); }
}