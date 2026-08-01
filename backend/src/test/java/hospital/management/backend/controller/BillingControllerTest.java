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
        BillingResponse response = new BillingResponse(1L, 1L, null, BigDecimal.valueOf(100.0), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(100.0), BigDecimal.ZERO, BigDecimal.valueOf(100.0), hospital.management.backend.enums.BillingStatus.PENDING);
        Mockito.when(billingService.create(Mockito.any(BillingRequest.class))).thenReturn(response);

        String json = "{\"patientId\":1,\"consultationFee\":100.0,\"medicineCharges\":0.0,\"otherCharges\":0.0}";

        mockMvc.perform(post("/api/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/billings/1"));
    }
}
