package hospital.management.backend.service;

import hospital.management.backend.dto.request.MedicalRecordRequest;
import hospital.management.backend.dto.response.MedicalRecordResponse;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordRequest request);
    List<MedicalRecordResponse> findAll();
    List<MedicalRecordResponse> findAllForDoctor(String doctorUsername);
    MedicalRecordResponse findById(Long id);
    MedicalRecordResponse update(Long id, MedicalRecordRequest request);
    void delete(Long id);
}