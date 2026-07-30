package hospital.management.backend.audit.aspect;

import hospital.management.backend.audit.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditLogService auditLogService;

    @Pointcut("execution(* hospital.management.backend.service.*.*(..)) || execution(* hospital.management.backend.service.impl.*.*(..))")
    public void serviceMethods() {}

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void auditServiceMethod(JoinPoint joinPoint, Object result) {
        String action = resolveAction(joinPoint);
        if (action == null) {
            return;
        }

        String user = "system";
        String ipAddress = "unknown";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if (request.getUserPrincipal() != null) {
                user = request.getUserPrincipal().getName();
            }
            ipAddress = request.getRemoteAddr();
        }

        String entityName = resolveEntityName(joinPoint);
        Long entityId = resolveEntityId(joinPoint, result);

        auditLogService.save(user, action, ipAddress, entityName, entityId);
    }

    private String resolveAction(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        return switch (methodName) {
            case "createPatient" -> "Patient Created";
            case "createDoctor" -> "Doctor Created";
            case "create" -> "Appointment Created";
            case "createPrescription" -> "Prescription Created";
            case "createBilling" -> "Bill Generated";
            default -> null;
        };
    }

    private String resolveEntityName(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        return switch (methodName) {
            case "createPatient" -> "Patient";
            case "createDoctor" -> "Doctor";
            case "create" -> "Appointment";
            case "createPrescription" -> "Prescription";
            case "createBilling" -> "Billing";
            default -> null;
        };
    }

    private Long resolveEntityId(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return null;
        }

        try {
            return (Long) Arrays.stream(result.getClass().getMethods())
                    .filter(method -> method.getName().equals("getId"))
                    .findFirst()
                    .orElseThrow()
                    .invoke(result);
        } catch (Exception ignored) {
            return null;
        }
    }
}
