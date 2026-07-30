package hospital.management.backend.audit;

import hospital.management.backend.audit.entity.AuditLog;
import hospital.management.backend.audit.repository.AuditLogRepository;
import hospital.management.backend.audit.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @Test
    void shouldSaveAuditLog() {
        AuditLog auditLog = AuditLog.builder()
                .id(1L)
                .user("admin")
                .action("Patient Created")
                .entityName("Patient")
                .entityId(10L)
                .build();

        when(auditLogRepository.save(org.mockito.ArgumentMatchers.any(AuditLog.class))).thenReturn(auditLog);

        AuditLog saved = auditLogService.save("admin", "Patient Created", "127.0.0.1", "Patient", 10L);

        assertThat(saved.getUser()).isEqualTo("admin");
        assertThat(saved.getAction()).isEqualTo("Patient Created");
        verify(auditLogRepository).save(org.mockito.ArgumentMatchers.any(AuditLog.class));
    }

    @Test
    void shouldFindAllAuditLogs() {
        when(auditLogRepository.findAll()).thenReturn(List.of(AuditLog.builder().id(1L).build()));

        List<AuditLog> logs = auditLogService.findAll();

        assertThat(logs).hasSize(1);
    }

    @Test
    void shouldFindAuditLogById() {
        AuditLog auditLog = AuditLog.builder().id(2L).build();
        when(auditLogRepository.findById(2L)).thenReturn(Optional.of(auditLog));

        AuditLog found = auditLogService.findById(2L);

        assertThat(found.getId()).isEqualTo(2L);
    }
}
