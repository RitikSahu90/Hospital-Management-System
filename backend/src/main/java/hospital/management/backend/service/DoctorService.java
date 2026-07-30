package hospital.management.backend.service;

import hospital.management.backend.dto.request.AvailabilityRequest;
import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.AvailabilityResponse;
import hospital.management.backend.dto.response.DoctorResponse;

import java.util.List;

public interface DoctorService {
    DoctorResponse create(DoctorRequest request);
    DoctorResponse update(Long id, DoctorRequest request);
    void delete(Long id);
    DoctorResponse findById(Long id);
    List<DoctorResponse> findAll();

    AvailabilityResponse addAvailability(Long doctorId, AvailabilityRequest request);
    List<AvailabilityResponse> getAvailability(Long doctorId);
}
