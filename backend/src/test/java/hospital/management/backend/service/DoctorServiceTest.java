package hospital.management.backend.service;

import hospital.management.backend.dto.request.AvailabilityRequest;
import hospital.management.backend.dto.request.DoctorRequest;
import hospital.management.backend.dto.response.AvailabilityResponse;
import hospital.management.backend.dto.response.DoctorResponse;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.DoctorAvailability;
import hospital.management.backend.mapper.DoctorMapper;
import hospital.management.backend.repository.DoctorAvailabilityRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.repository.UserRepository;
import hospital.management.backend.entity.Department;
import hospital.management.backend.entity.User;
import hospital.management.backend.enums.AvailabilityDay;
import hospital.management.backend.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorAvailabilityRepository availabilityRepository;

    @Mock
    private DoctorMapper doctorMapper;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void shouldCreateDoctor() {
        DoctorRequest request = new DoctorRequest();
        request.setFirstName("Neha");
        request.setLastName("Kumar");
        request.setLicenseNumber("LIC-1");
        request.setSpecialization("Neurology");

        Doctor entity = Doctor.builder().id(3L).firstName("Neha").lastName("Kumar").licenseNumber("LIC-1").specialization("Neurology").build();
        DoctorResponse response = new DoctorResponse(3L, null, null, null, "Neha", "Kumar", "LIC-1", "Neurology", null, null, null, null);

        when(doctorMapper.toEntity(request)).thenReturn(entity);
        when(departmentRepository.findById(any())).thenReturn(Optional.of(new Department()));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(doctorRepository.save(entity)).thenReturn(entity);
        when(doctorMapper.toResponse(entity)).thenReturn(response);

        DoctorResponse result = doctorService.create(request);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getSpecialization()).isEqualTo("Neurology");
        verify(doctorRepository).save(entity);
    }

    @Test
    void shouldUpdateDoctor() {
        DoctorRequest request = new DoctorRequest();
        request.setFirstName("Neha");
        request.setLastName("Kumar");
        request.setLicenseNumber("LIC-1");
        request.setSpecialization("Neurology");
        request.setPhone("9876543210");
        request.setConsultationFee(500.0);

        Doctor existing = Doctor.builder().id(3L).firstName("Old").lastName("Name").licenseNumber("LIC-1").specialization("Old").build();
        DoctorResponse response = new DoctorResponse(3L, null, null, null, "Neha", "Kumar", "LIC-1", "Neurology", "9876543210", null, 500.0, null);

        when(doctorRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findById(any())).thenReturn(Optional.of(new Department()));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(doctorRepository.save(existing)).thenReturn(existing);
        when(doctorMapper.toResponse(existing)).thenReturn(response);

        DoctorResponse result = doctorService.update(3L, request);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getFirstName()).isEqualTo("Neha");
        assertThat(result.getSpecialization()).isEqualTo("Neurology");
        assertThat(result.getPhone()).isEqualTo("9876543210");
        assertThat(result.getConsultationFee()).isEqualTo(500.0);
        verify(doctorRepository).save(existing);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentDoctor() {
        DoctorRequest request = new DoctorRequest();
        request.setFirstName("Neha");
        request.setLastName("Kumar");
        request.setLicenseNumber("LIC-1");
        request.setSpecialization("Neurology");

        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorService.update(99L, request));

        assertThat(exception.getMessage()).isEqualTo("Doctor not found");
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void shouldDeleteDoctor() {
        doctorService.delete(5L);

        verify(doctorRepository).deleteById(5L);
    }

    @Test
    void shouldFindDoctorById() {
        Doctor doctor = Doctor.builder().id(4L).firstName("Aman").lastName("Singh").specialization("Cardiology").build();
        when(doctorRepository.findById(4L)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toResponse(doctor)).thenReturn(new DoctorResponse(4L, null, null, null, "Aman", "Singh", null, "Cardiology", null, null, null, null));

        DoctorResponse result = doctorService.findById(4L);

        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getSpecialization()).isEqualTo("Cardiology");
    }

    @Test
    void shouldThrowWhenFindingNonExistentDoctor() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorService.findById(99L));

        assertThat(exception.getMessage()).isEqualTo("Doctor not found");
    }

    @Test
    void shouldFindAllDoctors() {
        Doctor doctor1 = Doctor.builder().id(1L).firstName("Aman").lastName("Singh").specialization("Cardiology").build();
        Doctor doctor2 = Doctor.builder().id(2L).firstName("Neha").lastName("Kumar").specialization("Neurology").build();
        when(doctorRepository.findAll()).thenReturn(List.of(doctor1, doctor2));
        when(doctorMapper.toResponse(doctor1)).thenReturn(new DoctorResponse(1L, null, null, null, "Aman", "Singh", null, "Cardiology", null, null, null, null));
        when(doctorMapper.toResponse(doctor2)).thenReturn(new DoctorResponse(2L, null, null, null, "Neha", "Kumar", null, "Neurology", null, null, null, null));

        List<DoctorResponse> result = doctorService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("Aman");
        assertThat(result.get(1).getFirstName()).isEqualTo("Neha");
    }

    @Test
    void shouldReturnEmptyListWhenNoDoctors() {
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        List<DoctorResponse> result = doctorService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldAddAndGetAvailability() {
        Doctor doctor = Doctor.builder().id(5L).firstName("Anita").lastName("Das").build();
        AvailabilityRequest request = new AvailabilityRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));

        when(doctorRepository.findById(5L)).thenReturn(Optional.of(doctor));
        DoctorAvailability availability = new DoctorAvailability();
        availability.setId(11L);
        availability.setDoctor(doctor);
        availability.setDayOfWeek(DayOfWeek.MONDAY);
        availability.setStartTime(LocalTime.of(9, 0));
        availability.setEndTime(LocalTime.of(12, 0));
        when(availabilityRepository.save(any(DoctorAvailability.class))).thenReturn(availability);
        when(availabilityRepository.findByDoctor(doctor)).thenReturn(List.of(availability));

        AvailabilityResponse created = doctorService.addAvailability(5L, request);
        List<AvailabilityResponse> list = doctorService.getAvailability(5L);

        assertThat(created.getDoctorId()).isEqualTo(5L);
        assertThat(created.getDayOfWeek()).isEqualTo(AvailabilityDay.MON);
        assertThat(list).hasSize(1);
        verify(availabilityRepository).save(any(DoctorAvailability.class));
    }

    @Test
    void shouldThrowWhenAddingAvailabilityForNonExistentDoctor() {
        AvailabilityRequest request = new AvailabilityRequest();
        request.setDayOfWeek(DayOfWeek.MONDAY);
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(12, 0));

        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorService.addAvailability(99L, request));

        assertThat(exception.getMessage()).isEqualTo("Doctor not found");
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenGettingAvailabilityForNonExistentDoctor() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorService.getAvailability(99L));

        assertThat(exception.getMessage()).isEqualTo("Doctor not found");
    }

    @Test
    void shouldReturnEmptyAvailabilityList() {
        Doctor doctor = Doctor.builder().id(5L).firstName("Anita").lastName("Das").build();
        when(doctorRepository.findById(5L)).thenReturn(Optional.of(doctor));
        when(availabilityRepository.findByDoctor(doctor)).thenReturn(Collections.emptyList());

        List<AvailabilityResponse> result = doctorService.getAvailability(5L);

        assertThat(result).isEmpty();
    }
}