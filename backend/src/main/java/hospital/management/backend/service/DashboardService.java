package hospital.management.backend.service;

import hospital.management.backend.dto.response.DashboardResponse;
import org.springframework.security.core.Authentication;

public interface DashboardService {
    DashboardResponse getSummary(Authentication authentication);
}