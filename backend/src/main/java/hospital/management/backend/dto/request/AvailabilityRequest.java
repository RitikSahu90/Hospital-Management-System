package hospital.management.backend.dto.request;

import hospital.management.backend.enums.AvailabilityDay;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class AvailabilityRequest {
    @NotNull
    private AvailabilityDay dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    public AvailabilityRequest() {
    }

    public AvailabilityDay getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(AvailabilityDay dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
