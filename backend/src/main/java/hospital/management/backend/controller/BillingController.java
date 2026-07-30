package hospital.management.backend.controller;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<BillingResponse> createBilling(@Valid @RequestBody BillingRequest request) {
        BillingResponse response = billingService.create(request);
        return ResponseEntity.created(URI.create("/api/billings/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<BillingResponse> updateBilling(@PathVariable Long id, @Valid @RequestBody BillingRequest request) {
        return ResponseEntity.ok(billingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        billingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<BillingResponse> getBilling(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<List<BillingResponse>> listBillings() {
        return ResponseEntity.ok(billingService.findAll());
    }
}
