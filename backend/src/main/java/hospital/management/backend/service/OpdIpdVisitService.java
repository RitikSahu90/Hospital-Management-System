package hospital.management.backend.service;

import hospital.management.backend.dto.request.OpdIpdVisitRequest;
import hospital.management.backend.dto.response.OpdIpdVisitResponse;

import java.util.List;

public interface OpdIpdVisitService {
    OpdIpdVisitResponse createVisit(OpdIpdVisitRequest request);
    OpdIpdVisitResponse updateVisit(Long id, OpdIpdVisitRequest request);
    OpdIpdVisitResponse findById(Long id);
    List<OpdIpdVisitResponse> findAll();
    List<OpdIpdVisitResponse> findByPatientId(Long patientId);
    void deleteVisit(Long id);
}
