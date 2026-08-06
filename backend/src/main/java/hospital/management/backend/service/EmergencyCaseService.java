package hospital.management.backend.service;

import hospital.management.backend.dto.request.EmergencyCaseRequest;
import hospital.management.backend.dto.response.EmergencyCaseResponse;

import java.util.List;

public interface EmergencyCaseService {
    EmergencyCaseResponse createEmergencyCase(EmergencyCaseRequest request);
    EmergencyCaseResponse updateEmergencyCase(Long id, EmergencyCaseRequest request);
    EmergencyCaseResponse findById(Long id);
    List<EmergencyCaseResponse> findAll();
    List<EmergencyCaseResponse> findByPatientId(Long patientId);
    void deleteEmergencyCase(Long id);
}
