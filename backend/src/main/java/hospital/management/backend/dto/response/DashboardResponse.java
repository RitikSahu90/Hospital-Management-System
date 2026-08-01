package hospital.management.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private long patientCount;
    private long doctorCount;
    private long appointmentCount;
    private BigDecimal revenue;
    private Map<String, Long> appointmentsByStatus;
}