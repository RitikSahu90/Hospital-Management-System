package hospital.management.backend.controller;

import hospital.management.backend.audit.entity.AuditLog;
import hospital.management.backend.audit.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import hospital.management.backend.config.TestSecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(hospital.management.backend.audit.controller.AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditLogService auditLogService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllAuditLogs() throws Exception {
        when(auditLogService.findAll()).thenReturn(List.of(AuditLog.builder().id(1L).user("admin").action("Patient Created").timestamp(LocalDateTime.now()).build()));

        mockMvc.perform(get("/api/audit"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAuditLogById() throws Exception {
        when(auditLogService.findById(1L)).thenReturn(AuditLog.builder().id(1L).user("admin").action("Patient Created").timestamp(LocalDateTime.now()).build());

        mockMvc.perform(get("/api/audit/1"))
                .andExpect(status().isOk());
    }
}
