package hospital.management.backend.service;

import hospital.management.backend.dto.response.PatientResponse;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(patient -> new PatientResponse(
                        patient.getId(),
                        patient.getPatientNumber(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getEmail(),
                        patient.getPhone(),
                        patient.getDateOfBirth(),
                        patient.getGender(),
                        patient.getAddress(),
                        patient.getBloodGroup()))
                .toList();
    }

    public PatientResponse findForUsername(String username) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found"));
        return toResponse(patient);
    }

    public boolean belongsToUser(Long patientId, String username) {
        return patientRepository.findById(patientId)
                .map(patient -> patient.getUser() != null && patient.getUser().getUsername().equals(username))
                .orElse(false);
    }

    public PatientResponse create(Patient patient) {
        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    public PatientResponse update(Long id, Patient patient) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        existing.setFirstName(patient.getFirstName());
        existing.setLastName(patient.getLastName());
        existing.setEmail(patient.getEmail());
        existing.setPhone(patient.getPhone());
        existing.setPatientNumber(patient.getPatientNumber());
        existing.setDateOfBirth(patient.getDateOfBirth());
        existing.setGender(patient.getGender());
        existing.setAddress(patient.getAddress());
        existing.setBloodGroup(patient.getBloodGroup());
        return toResponse(patientRepository.save(existing));
    }

    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    private PatientResponse toResponse(Patient saved) {
        return new PatientResponse(
                saved.getId(),
                saved.getPatientNumber(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getAddress(),
                saved.getBloodGroup());
    }
}
