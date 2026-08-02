package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.MedicalRecordRequest;
import hospital.management.backend.dto.response.MedicalRecordResponse;
import hospital.management.backend.entity.MedicalRecord;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.MedicalRecordRepository;
import hospital.management.backend.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository recordRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        MedicalRecord record = new MedicalRecord();
        apply(record, request);
        return toResponse(recordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> findAll() { return recordRepository.findAll().stream().map(this::toResponse).toList(); }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse findById(Long id) { return toResponse(recordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Medical record not found"))); }

    @Override
    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        MedicalRecord record = recordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Medical record not found"));
        apply(record, request);
        return toResponse(recordRepository.save(record));
    }

    @Override
    public void delete(Long id) {
        if (!recordRepository.existsById(id)) throw new IllegalArgumentException("Medical record not found");
        recordRepository.deleteById(id);
    }

    private void apply(MedicalRecord record, MedicalRecordRequest request) {
        record.setAppointment(appointmentRepository.findById(request.getAppointmentId()).orElseThrow(() -> new IllegalArgumentException("Appointment not found")));
        record.setDiagnosis(request.getDiagnosis());
        record.setClinicalNotes(request.getClinicalNotes());
    }

    private MedicalRecordResponse toResponse(MedicalRecord record) {
        return new MedicalRecordResponse(record.getId(), record.getAppointment().getId(), record.getAppointment().getPatient().getId(), record.getAppointment().getDoctor().getId(), record.getDiagnosis(), record.getClinicalNotes(), record.getCreatedAt(), record.getUpdatedAt());
    }
}