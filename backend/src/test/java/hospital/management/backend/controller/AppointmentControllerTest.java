package hospital.management.backend.controller;

import hospital.management.backend.dto.request.AppointmentRequest;
import hospital.management.backend.dto.response.AppointmentResponse;
import hospital.management.backend.enums.AppointmentStatus;
import hospital.management.backend.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import hospital.management.backend.config.TestSecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    @WithMockUser
    void shouldGetAppointments() throws Exception {
        when(appointmentService.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldBookAppointment() throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1L);
        request.setDoctorId(2L);
        request.setAppointmentDate(LocalDate.of(2026, 8, 1));
        request.setAppointmentTime(LocalTime.of(10, 0));

        when(appointmentService.bookAppointment(org.mockito.ArgumentMatchers.any(AppointmentRequest.class)))
                .thenReturn(new AppointmentResponse(1L, 1L, 2L, request.getAppointmentDate(), request.getAppointmentTime(), AppointmentStatus.SCHEDULED, null));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":1,\"doctorId\":2,\"appointmentDate\":\"2026-08-01\",\"appointmentTime\":\"10:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
