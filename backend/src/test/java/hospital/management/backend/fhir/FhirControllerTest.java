package hospital.management.backend.fhir;

import hospital.management.backend.fhir.controller.FhirController;
import hospital.management.backend.fhir.dto.AppointmentFhirResource;
import hospital.management.backend.fhir.dto.PatientFhirResource;
import hospital.management.backend.fhir.dto.PractitionerFhirResource;
import hospital.management.backend.fhir.service.FhirService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import hospital.management.backend.config.TestSecurityConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FhirController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class FhirControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FhirService fhirService;

    @Test
    void shouldGetPatientResource() throws Exception {
        when(fhirService.getPatient(1L)).thenReturn(PatientFhirResource.builder().id(1L).name("Asha Patel").build());

        mockMvc.perform(get("/fhir/Patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asha Patel"));
    }

    @Test
    void shouldGetPractitionerResource() throws Exception {
        when(fhirService.getPractitioner(2L)).thenReturn(PractitionerFhirResource.builder().id(2L).name("Ravi Sharma").build());

        mockMvc.perform(get("/fhir/Practitioner/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ravi Sharma"));
    }

    @Test
    void shouldGetAppointmentResource() throws Exception {
        when(fhirService.getAppointment(3L)).thenReturn(AppointmentFhirResource.builder().id(3L).status("SCHEDULED").build());

        mockMvc.perform(get("/fhir/Appointment/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }
}
