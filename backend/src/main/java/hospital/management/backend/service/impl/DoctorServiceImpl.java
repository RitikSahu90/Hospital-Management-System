package hospital.management.backend.service.impl;

import hospital.management.backend.dto.request.AvailabilityRequest;
import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.AvailabilityResponse;
import hospital.management.backend.dto.response.DoctorResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.DoctorAvailability;
import hospital.management.backend.entity.Department;
import hospital.management.backend.mapper.DoctorMapper;
import hospital.management.backend.repository.DoctorAvailabilityRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.repository.UserRepository;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper mapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorAvailabilityRepository availabilityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, DoctorMapper mapper) {
        this.doctorRepository = doctorRepository;
        this.availabilityRepository = availabilityRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public DoctorResponse create(DoctorRequest request) {
        Doctor d = mapper.toEntity(request);
        d.setDepartment(departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found")));
        
        if (request.getUserId() != null) {
            d.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        } else {
            Role role = roleRepository.findByName("DOCTOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            User user = User.builder()
                    .username((request.getFirstName() + request.getLastName()).toLowerCase().replaceAll("\\s+", "") + System.currentTimeMillis() % 1000)
                    .email(request.getFirstName().toLowerCase() + "." + request.getLastName().toLowerCase() + "@hospital.com")
                    .password(passwordEncoder.encode("pass123"))
                    .role(role)
                    .build();
            userRepository.save(user);
            d.setUser(user);
        }
        
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
        existing.setDoctorCode(request.getDoctorCode());
        existing.setYearsExperience(request.getYearsExperience());
        existing.setStatus(request.getStatus());
        existing.setDepartment(departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found")));
            
        if (request.getUserId() != null) {
            existing.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }
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

    @Override
    public AvailabilityResponse updateAvailability(Long doctorId, Long availabilityId, AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .filter(item -> item.getDoctor().getId().equals(doctor.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Availability not found"));
        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        DoctorAvailability saved = availabilityRepository.save(availability);
        return new AvailabilityResponse(saved.getId(), doctor.getId(), saved.getDayOfWeek(), saved.getStartTime(), saved.getEndTime());
    }

    @Override
    public void deleteAvailability(Long doctorId, Long availabilityId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .filter(item -> item.getDoctor().getId().equals(doctor.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Availability not found"));
        availabilityRepository.delete(availability);
    }
}
