package hospital.management.backend.service;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;

import java.util.List;

public interface BillingService {
    BillingResponse create(BillingRequest request);
    BillingResponse update(Long id, BillingRequest request);
    void delete(Long id);
    BillingResponse findById(Long id);
    List<BillingResponse> findAll();
}
