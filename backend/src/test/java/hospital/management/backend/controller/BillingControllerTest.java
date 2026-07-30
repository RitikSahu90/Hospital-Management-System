package hospital.management.backend.controller;

import hospital.management.backend.dto.request.BillingRequest;
import hospital.management.backend.dto.response.BillingResponse;
import hospital.management.backend.security.JwtAuthenticationFilter;
import hospital.management.backend.security.jwt.JwtUtil;
import hospital.management.backend.service.BillingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BillingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateBilling() throws Exception {
        BillingResponse response = new BillingResponse(1L, 1L, 2L, BigDecimal.valueOf(100.0), BigDecimal.valueOf(80.0), BigDecimal.valueOf(20.0), LocalDate.now(), false);
        Mockito.when(billingService.create(Mockito.any(BillingRequest.class))).thenReturn(response);

        String json = "{\"patientId\":1,\"prescriptionId\":2,\"totalAmount\":100.0,\"paidAmount\":80.0,\"billingDate\":\"" + LocalDate.now() + "\"}";

        mockMvc.perform(post("/api/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/billings/1"));
    }
}
