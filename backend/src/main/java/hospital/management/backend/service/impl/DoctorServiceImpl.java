package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.AvailabilityRequest;
import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.AvailabilityResponse;
import hospital.management.backend.dto.response.DoctorResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.DoctorAvailability;
import hospital.management.backend.mapper.DoctorMapper;
import hospital.management.backend.repository.DoctorAvailabilityRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorMapper mapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorAvailabilityRepository availabilityRepository, DoctorMapper mapper) {
        this.doctorRepository = doctorRepository;
        this.availabilityRepository = availabilityRepository;
        this.mapper = mapper;
    }

    @Override
    public DoctorResponse create(DoctorRequest request) {
        Doctor d = mapper.toEntity(request);
        Doctor saved = doctorRepository.save(d);
        return mapper.toResponse(saved);
    }

    @Override
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor existing = doctorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setLicenseNumber(request.getLicenseNumber());
        existing.setSpecialization(request.getSpecialization());
        existing.setPhone(request.getPhone());
        existing.setConsultationFee(request.getConsultationFee());
        Doctor saved = doctorRepository.save(existing);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public DoctorResponse findById(Long id) {
        Doctor d = doctorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return mapper.toResponse(d);
    }

    @Override
    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public AvailabilityResponse addAvailability(Long doctorId, AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        DoctorAvailability avail = new DoctorAvailability();
        avail.setDoctor(doctor);
        avail.setDayOfWeek(request.getDayOfWeek());
        avail.setStartTime(request.getStartTime());
        avail.setEndTime(request.getEndTime());
        DoctorAvailability saved = availabilityRepository.save(avail);
        return new AvailabilityResponse(saved.getId(), doctor.getId(), saved.getDayOfWeek(), saved.getStartTime(), saved.getEndTime());
    }

    @Override
    public List<AvailabilityResponse> getAvailability(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return availabilityRepository.findByDoctor(doctor).stream()
                .map(a -> new AvailabilityResponse(a.getId(), doctor.getId(), a.getDayOfWeek(), a.getStartTime(), a.getEndTime()))
                .collect(Collectors.toList());
    }
}
