package hospital.management.backend.dto.response;

import hospital.management.backend.enums.AvailabilityDay;
import java.time.LocalTime;

public class AvailabilityResponse {
    private Long id;
    private Long doctorId;
    private AvailabilityDay dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public AvailabilityResponse() {
    }

    public AvailabilityResponse(Long id, Long doctorId, AvailabilityDay dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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
