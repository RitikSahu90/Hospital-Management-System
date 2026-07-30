package hospital.management.backend.controller;

import hospital.management.backend.dto.request.MedicineRequest;
import hospital.management.backend.dto.response.MedicineResponse;
import hospital.management.backend.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<MedicineResponse> createMedicine(@Valid @RequestBody MedicineRequest request) {
        MedicineResponse response = medicineService.create(request);
        return ResponseEntity.created(URI.create("/api/medicines/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Long id, @Valid @RequestBody MedicineRequest request) {
        return ResponseEntity.ok(medicineService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<MedicineResponse> getMedicine(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<List<MedicineResponse>> listMedicines() {
        return ResponseEntity.ok(medicineService.findAll());
    }

    @PostMapping("/{id}/stock/reduce")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<MedicineResponse> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(medicineService.reduceStock(id, quantity));
    }

    @PostMapping("/{id}/stock/increase")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<MedicineResponse> increaseStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(medicineService.increaseStock(id, quantity));
    }
}
