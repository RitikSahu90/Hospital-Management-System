package hospital.management.backend.controller;

import hospital.management.backend.dto.request.StaffRegistrationRequest;
import hospital.management.backend.entity.Department;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.User;
import hospital.management.backend.enums.DoctorStatus;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> registerStaff(@Valid @RequestBody StaffRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is already taken!"));
        }

        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode("pass123"))
                .role(role)
                .build();
        
        userRepository.save(user);

        if ("DOCTOR".equalsIgnoreCase(request.getRole())) {
            Department dept = null;
            if (request.getDepartmentId() != null) {
                dept = departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Error: Department not found."));
            }
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .department(dept)
                    .doctorCode(request.getDoctorCode())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .licenseNumber(request.getLicenseNumber())
                    .specialization(request.getSpecialization())
                    .phone(request.getPhone())
                    .consultationFee(request.getConsultationFee() != null ? request.getConsultationFee() : 0.0)
                    .yearsExperience(request.getYearsExperience() != null ? request.getYearsExperience() : 0)
                    .status(DoctorStatus.ACTIVE)
                    .build();
            doctorRepository.save(doctor);
        }

        return ResponseEntity.ok(Map.of("message", "Staff registered successfully!"));
    }
}
