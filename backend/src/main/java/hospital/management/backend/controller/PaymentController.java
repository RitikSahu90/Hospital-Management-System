package hospital.management.backend.controller;

import hospital.management.backend.dto.request.PaymentRequest;
import hospital.management.backend.dto.response.PaymentResponse;
import hospital.management.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/bill/{billId}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<PaymentResponse> create(@PathVariable Long billId,
                                                  @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.create(billId, request);
        return ResponseEntity.created(URI.create("/api/payments/" + response.getId())).body(response);
    }

    @GetMapping("/bill/{billId}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<List<PaymentResponse>> list(@PathVariable Long billId) {
        return ResponseEntity.ok(paymentService.findByBill(billId));
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public ResponseEntity<PaymentResponse> get(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.findById(paymentId));
    }
}