package hospital.management.backend.config;

import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.User;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
        Role userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole, userRole))
                    .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .firstName("Asha")
                    .lastName("Patel")
                    .email("asha@example.com")
                    .phone("9876543210")
                    .diagnosis("Hypertension")
                    .build());
        }
    }
}
