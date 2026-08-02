package hospital.management.backend.controller;

import hospital.management.backend.dto.request.DepartmentRequest;
import hospital.management.backend.dto.response.DepartmentResponse;
import hospital.management.backend.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) { DepartmentResponse response = service.create(request); return ResponseEntity.created(URI.create("/api/departments/" + response.getId())).body(response); }
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<DepartmentResponse> list() { return service.findAll(); }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public DepartmentResponse get(@PathVariable Long id) { return service.findById(id); }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public DepartmentResponse update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}