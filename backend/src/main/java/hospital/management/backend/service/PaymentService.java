package hospital.management.backend.service;

import hospital.management.backend.dto.request.PaymentRequest;
import hospital.management.backend.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse create(Long billId, PaymentRequest request);
    List<PaymentResponse> findByBill(Long billId);
    PaymentResponse findById(Long id);
}