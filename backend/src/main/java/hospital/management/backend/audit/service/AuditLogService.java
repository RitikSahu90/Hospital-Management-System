package hospital.management.backend.audit.service;

import hospital.management.backend.audit.entity.AuditLog;
import hospital.management.backend.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLog save(String user, String action, String ipAddress, String entityName, Long entityId) {
        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .timestamp(LocalDateTime.now())
                .ipAddress(ipAddress)
                .entityName(entityName)
                .entityId(entityId)
                .build();
        return auditLogRepository.save(log);
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

    public AuditLog findById(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Audit log not found"));
    }
}
