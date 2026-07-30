package hospital.management.backend.controller;

import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

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
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        billingRepository.deleteAll();
        prescriptionRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreatePrescriptionAndPersist() throws Exception {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john@example.com");
        patient = patientRepository.save(patient);

        Doctor doctor = new Doctor();
        doctor.setFirstName("Dr.");
        doctor.setLastName("Smith");
        doctor.setLicenseNumber("LIC123");
        doctor.setSpecialization("General Medicine");
        doctor = doctorRepository.save(doctor);

        String json = "{\"patientId\":" + patient.getId() + ",\"doctorId\":" + doctor.getId() + ",\"medicineName\":\"Amoxicillin\",\"dosage\":\"500mg\",\"frequency\":\"Twice a day\",\"durationDays\":7,\"prescribedDate\":\"" + LocalDate.now() + "\",\"notes\":\"Take with food\"}";

        mockMvc.perform(post("/api/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        Prescription saved = prescriptionRepository.findAll().get(0);
        assertThat(saved.getMedicineName()).isEqualTo("Amoxicillin");
        assertThat(saved.getPatient().getId()).isEqualTo(patient.getId());
        assertThat(saved.getDoctor().getId()).isEqualTo(doctor.getId());
    }
}
