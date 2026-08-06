package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.EmergencyCaseRequest;
import hospital.management.backend.dto.response.EmergencyCaseResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.EmergencyCase;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.enums.EmergencyStatus;
import hospital.management.backend.enums.TriageLevel;
import hospital.management.backend.mapper.EmergencyCaseMapper;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.EmergencyCaseRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.service.EmergencyCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmergencyCaseServiceImpl implements EmergencyCaseService {
    private final EmergencyCaseRepository emergencyCaseRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EmergencyCaseMapper mapper;

    @Override
    public EmergencyCaseResponse createEmergencyCase(EmergencyCaseRequest request) {
        Patient patient = null;
        if (request.getPatientId() != null) {
            patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        }

        Doctor doctor = null;
        if (request.getAssignedDoctorId() != null) {
            doctor = doctorRepository.findById(request.getAssignedDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        }

        EmergencyCase ec = mapper.toEntity(request);
        ec.setPatient(patient);
        ec.setAssignedDoctor(doctor);
        ec.setArrivalTime(Instant.now());

        EmergencyCase saved = emergencyCaseRepository.save(ec);
        return mapper.toResponse(saved);
    }

    @Override
    public EmergencyCaseResponse updateEmergencyCase(Long id, EmergencyCaseRequest request) {
        EmergencyCase ec = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emergency case not found"));

        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
            ec.setPatient(patient);
        } else if (request.getPatientId() == null) {
            ec.setPatient(null);
        }

        if (request.getAssignedDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getAssignedDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
            ec.setAssignedDoctor(doctor);
        } else if (request.getAssignedDoctorId() == null) {
            ec.setAssignedDoctor(null);
        }

        if (request.getTriageLevel() != null) {
            ec.setTriageLevel(TriageLevel.valueOf(request.getTriageLevel().toUpperCase()));
        }

        if (request.getStatus() != null) {
            EmergencyStatus newStatus = EmergencyStatus.valueOf(request.getStatus().toUpperCase());
            ec.setStatus(newStatus);
            if (List.of(EmergencyStatus.DISCHARGED, EmergencyStatus.TRANSFERRED, EmergencyStatus.DECEASED).contains(newStatus)) {
                ec.setResolvedAt(Instant.now());
            }
        }

        ec.setChiefComplaint(request.getChiefComplaint());
        ec.setResolutionNotes(request.getResolutionNotes());

        EmergencyCase saved = emergencyCaseRepository.save(ec);
        return mapper.toResponse(saved);
    }

    @Override
    public EmergencyCaseResponse findById(Long id) {
        EmergencyCase ec = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emergency case not found"));
        return mapper.toResponse(ec);
    }

    @Override
    public List<EmergencyCaseResponse> findAll() {
        return emergencyCaseRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyCaseResponse> findByPatientId(Long patientId) {
        return emergencyCaseRepository.findByPatientId(patientId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEmergencyCase(Long id) {
        if (!emergencyCaseRepository.existsById(id)) {
            throw new IllegalArgumentException("Emergency case not found");
        }
        emergencyCaseRepository.deleteById(id);
    }
}
