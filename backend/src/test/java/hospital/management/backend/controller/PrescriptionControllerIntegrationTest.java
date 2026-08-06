package hospital.management.backend.controller;

import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.entity.User;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.Department;
import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.MedicalRecord;
import hospital.management.backend.enums.AppointmentStatus;
import hospital.management.backend.enums.DepartmentStatus;
import hospital.management.backend.enums.DoctorStatus;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.repository.UserRepository;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.MedicalRecordRepository;
import hospital.management.backend.repository.PaymentRepository;
import hospital.management.backend.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PrescriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        paymentRepository.deleteAll();
        billingRepository.deleteAll();
        prescriptionRepository.deleteAll();
        medicalRecordRepository.deleteAll();
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreatePrescriptionAndPersist() throws Exception {
        Patient patient = new Patient();
        patient.setPatientNumber("P-RX-1"); patient.setFirstName("John");
        patient.setLastName("Doe"); patient.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1)); patient.setGender(hospital.management.backend.enums.Gender.MALE); patient.setPhone("9999999999");
        patient.setEmail("john@example.com");
        patient = patientRepository.save(patient);

        Role role = roleRepository.findByName("DOCTOR").orElseGet(() -> roleRepository.save(Role.builder().name("DOCTOR").build()));
        User user = userRepository.save(User.builder().username("rx-doctor").email("rx-doctor@example.com").password("encoded").role(role).build());
        Department department = departmentRepository.save(Department.builder().name("General").code("GEN").status(DepartmentStatus.ACTIVE).build());
        Doctor doctor = doctorRepository.save(Doctor.builder().user(user).department(department).doctorCode("RX-1").firstName("Dr.").lastName("Smith").licenseNumber("LIC123").specialization("General Medicine").phone("9999999998").yearsExperience(1).status(DoctorStatus.ACTIVE).consultationFee(100.0).build());
        Appointment appointment = appointmentRepository.save(Appointment.builder().patient(patient).doctor(doctor).appointmentDate(java.time.LocalDate.now()).appointmentTime(java.time.LocalTime.NOON).status(AppointmentStatus.COMPLETED).build());
        MedicalRecord record = medicalRecordRepository.save(MedicalRecord.builder().appointment(appointment).diagnosis("Routine").build());
        String json = "{\"patientId\":" + patient.getId() + ",\"doctorId\":" + doctor.getId() + ",\"medicalRecordId\":" + record.getId() + ",\"items\":[],\"notes\":\"Take with food\"}";

        mockMvc.perform(post("/api/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        Prescription saved = prescriptionRepository.findAll().get(0);
        assertThat(saved.getPatient().getId()).isEqualTo(patient.getId());
    }
}
