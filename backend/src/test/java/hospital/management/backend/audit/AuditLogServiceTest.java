package hospital.management.backend.audit;

import hospital.management.backend.audit.entity.AuditLog;
import hospital.management.backend.audit.repository.AuditLogRepository;
import hospital.management.backend.audit.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

        AuditLog saved = auditLogService.save("admin", "Patient Created", "127.0.0.1", "Patient", 10L);

        assertThat(saved.getUser()).isEqualTo("admin");
        assertThat(saved.getAction()).isEqualTo("Patient Created");
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void shouldPopulateAllFieldsWhenSaving() {
        AuditLog auditLog = AuditLog.builder()
                .id(1L)
                .user("doctor")
                .action("Appointment Updated")
                .ipAddress("192.168.1.1")
                .entityName("Appointment")
                .entityId(20L)
                .build();

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

        AuditLog saved = auditLogService.save("doctor", "Appointment Updated", "192.168.1.1", "Appointment", 20L);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());
        AuditLog captured = captor.getValue();

        assertThat(captured.getUser()).isEqualTo("doctor");
        assertThat(captured.getAction()).isEqualTo("Appointment Updated");
        assertThat(captured.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(captured.getEntityName()).isEqualTo("Appointment");
        assertThat(captured.getEntityId()).isEqualTo(20L);
        assertThat(captured.getTimestamp()).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindAllAuditLogs() {
        when(auditLogRepository.findAll()).thenReturn(List.of(AuditLog.builder().id(1L).build()));

        List<AuditLog> logs = auditLogService.findAll();

        assertThat(logs).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenNoAuditLogs() {
        when(auditLogRepository.findAll()).thenReturn(Collections.emptyList());

        List<AuditLog> logs = auditLogService.findAll();

        assertThat(logs).isEmpty();
    }

    @Test
    void shouldFindAuditLogById() {
        AuditLog auditLog = AuditLog.builder().id(2L).build();
        when(auditLogRepository.findById(2L)).thenReturn(Optional.of(auditLog));

        AuditLog found = auditLogService.findById(2L);

        assertThat(found.getId()).isEqualTo(2L);
    }

    @Test
    void shouldThrowWhenAuditLogNotFound() {
        when(auditLogRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> auditLogService.findById(99L));

        assertThat(exception.getMessage()).isEqualTo("Audit log not found");
    }
}