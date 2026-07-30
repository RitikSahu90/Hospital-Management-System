package hospital.management.backend.controller;

import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Prescription;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PrescriptionRepository;
import hospital.management.backend.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BillingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @BeforeEach
    void setUp() {
        billingRepository.deleteAll();
        patientRepository.deleteAll();
        prescriptionRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateBillingAndPersist() throws Exception {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john@example.com");
        patient = patientRepository.save(patient);

        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setLicenseNumber("LIC789");
        doctor.setSpecialization("General Practice");
        doctor = doctorRepository.save(doctor);

        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setMedicineName("Amoxicillin");
        prescription.setDosage("500mg");
        prescription.setFrequency("Twice a day");
        prescription.setDurationDays(7);
        prescription.setPrescribedDate(LocalDate.now());
        prescription = prescriptionRepository.save(prescription);

        String json = "{\"patientId\":" + patient.getId() + ",\"prescriptionId\":" + prescription.getId() + ",\"totalAmount\":100.0,\"paidAmount\":80.0,\"billingDate\":\"" + LocalDate.now() + "\"}";

        mockMvc.perform(post("/api/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        assertThat(billingRepository.findAll()).hasSize(1);
    }
}
