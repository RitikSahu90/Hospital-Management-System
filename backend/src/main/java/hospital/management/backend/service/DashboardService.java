package hospital.management.backend.service;

import hospital.management.backend.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getSummary();
}