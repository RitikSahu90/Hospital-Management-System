package hospital.management.backend.config;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.User;
import hospital.management.backend.entity.Department;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.enums.DepartmentStatus;
import hospital.management.backend.enums.DoctorStatus;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
        roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
        roleRepository.findByName("PATIENT").orElseGet(() -> roleRepository.save(Role.builder().name("PATIENT").build()));
        Role doctorRole = roleRepository.findByName("DOCTOR").orElseGet(() -> roleRepository.save(Role.builder().name("DOCTOR").build()));
        roleRepository.findByName("RECEPTIONIST").orElseGet(() -> roleRepository.save(Role.builder().name("RECEPTIONIST").build()));
        roleRepository.findByName("PHARMACIST").orElseGet(() -> roleRepository.save(Role.builder().name("PHARMACIST").build()));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(adminRole)
                    .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .patientNumber("P-0001")
                    .firstName("Asha")
                    .lastName("Patel")
                    .dateOfBirth(java.time.LocalDate.of(1990, 1, 1))
                    .gender(hospital.management.backend.enums.Gender.FEMALE)
                    .email("asha@example.com")
                    .phone("9876543210")
                    .build());
        }

                Department department = departmentRepository.findAll().stream().findFirst().orElseGet(() -> departmentRepository.save(Department.builder()
                    .name("Cardiology").code("CARD").description("Cardiology department").status(DepartmentStatus.ACTIVE).build()));
                User doctorUser = userRepository.findByUsername("doctor").orElseGet(() -> userRepository.save(User.builder()
                    .username("doctor").email("doctor@example.com").password(passwordEncoder.encode("doctor123"))
                    .role(doctorRole).build()));
                if (!doctorRepository.existsByDoctorCode("DOC-0001")) {
                    doctorRepository.save(Doctor.builder().user(doctorUser).department(department).doctorCode("DOC-0001")
                        .firstName("Ravi").lastName("Sharma").licenseNumber("LIC-0001")
                        .specialization("Cardiology").phone("9876543211").yearsExperience(10)
                        .status(DoctorStatus.ACTIVE).consultationFee(1000.0).build());
                }
    }
}
