package hospital.management.backend.controller;

import hospital.management.backend.dto.request.SupplierRequest;
import hospital.management.backend.dto.response.SupplierResponse;
import hospital.management.backend.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService service;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<SupplierResponse> create(@Valid @RequestBody SupplierRequest request) { SupplierResponse response = service.create(request); return ResponseEntity.created(URI.create("/api/suppliers/" + response.getId())).body(response); }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public List<SupplierResponse> list() { return service.findAll(); }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public SupplierResponse get(@PathVariable Long id) { return service.findById(id); }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public SupplierResponse update(@PathVariable Long id, @Valid @RequestBody SupplierRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}