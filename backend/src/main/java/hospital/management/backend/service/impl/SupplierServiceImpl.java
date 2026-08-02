package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.SupplierRequest;
import hospital.management.backend.dto.response.SupplierResponse;
import hospital.management.backend.entity.Supplier;
import hospital.management.backend.repository.SupplierRepository;
import hospital.management.backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository repository;

    public SupplierResponse create(SupplierRequest request) { Supplier supplier = new Supplier(); apply(supplier, request); return toResponse(repository.save(supplier)); }
    @Transactional(readOnly = true)
    public List<SupplierResponse> findAll() { return repository.findAll().stream().map(this::toResponse).toList(); }
    @Transactional(readOnly = true)
    public SupplierResponse findById(Long id) { return toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Supplier not found"))); }
    public SupplierResponse update(Long id, SupplierRequest request) { Supplier supplier = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Supplier not found")); apply(supplier, request); return toResponse(repository.save(supplier)); }
    public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Supplier not found"); repository.deleteById(id); }

    private void apply(Supplier supplier, SupplierRequest request) { supplier.setName(request.getName()); supplier.setContactPerson(request.getContactPerson()); supplier.setPhone(request.getPhone()); supplier.setEmail(request.getEmail()); supplier.setAddress(request.getAddress()); }
    private SupplierResponse toResponse(Supplier supplier) { return new SupplierResponse(supplier.getId(), supplier.getName(), supplier.getContactPerson(), supplier.getPhone(), supplier.getEmail(), supplier.getAddress()); }
}