package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.OpdIpdVisitRequest;
import hospital.management.backend.dto.response.OpdIpdVisitResponse;
import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.OpdIpdVisit;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.enums.VisitStatus;
import hospital.management.backend.enums.VisitType;
import hospital.management.backend.mapper.OpdIpdVisitMapper;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.OpdIpdVisitRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.service.OpdIpdVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OpdIpdVisitServiceImpl implements OpdIpdVisitService {
    private final OpdIpdVisitRepository opdIpdVisitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final OpdIpdVisitMapper mapper;

    @Override
    public OpdIpdVisitResponse createVisit(OpdIpdVisitRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        
        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        }

        OpdIpdVisit visit = mapper.toEntity(request);
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setAppointment(appointment);

        OpdIpdVisit saved = opdIpdVisitRepository.save(visit);
        return mapper.toResponse(saved);
    }

    @Override
    public OpdIpdVisitResponse updateVisit(Long id, OpdIpdVisitRequest request) {
        OpdIpdVisit visit = opdIpdVisitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OPD/IPD Visit not found"));

        if (request.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
            visit.setDoctor(doctor);
        }

        if (request.getVisitType() != null) {
            visit.setVisitType(VisitType.valueOf(request.getVisitType().toUpperCase()));
        }

        if (request.getStatus() != null) {
            visit.setStatus(VisitStatus.valueOf(request.getStatus().toUpperCase()));
        }

        visit.setChiefComplaint(request.getChiefComplaint());
        visit.setWard(request.getWard());
        visit.setBedNumber(request.getBedNumber());
        visit.setAdmissionDate(request.getAdmissionDate());
        visit.setDischargeDate(request.getDischargeDate());
        visit.setNotes(request.getNotes());

        OpdIpdVisit saved = opdIpdVisitRepository.save(visit);
        return mapper.toResponse(saved);
    }

    @Override
    public OpdIpdVisitResponse findById(Long id) {
        OpdIpdVisit visit = opdIpdVisitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OPD/IPD Visit not found"));
        return mapper.toResponse(visit);
    }

    @Override
    public List<OpdIpdVisitResponse> findAll() {
        return opdIpdVisitRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OpdIpdVisitResponse> findByPatientId(Long patientId) {
        return opdIpdVisitRepository.findByPatientId(patientId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVisit(Long id) {
        if (!opdIpdVisitRepository.existsById(id)) {
            throw new IllegalArgumentException("OPD/IPD Visit not found");
        }
        opdIpdVisitRepository.deleteById(id);
    }
}
