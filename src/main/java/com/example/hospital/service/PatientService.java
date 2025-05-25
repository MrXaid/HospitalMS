package com.example.hospital.service;

import com.example.hospital.converter.PatientConverter;
import com.example.hospital.dto.PatientDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.Patient;
import com.example.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Existing CRUD methods...

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        existingPatient.setFullName(updatedPatient.getFullName());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setAddress(updatedPatient.getAddress());
        // Appointments usually updated separately

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    // Add this authenticate method:
    public PatientDTO authenticate(String email, String password) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        Patient patient = patientOpt.get();

        if (!patient.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        return PatientConverter.entityToDto(patient);
    }


    // Register patient with password encoding if you want secure signup
    public void registerPatient(PatientDTO patientDto) {
        // Convert DTO to entity
        Patient patient = new Patient();
        patient.setFullName(patientDto.getFullName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setGender(patientDto.getGender());
        patient.setAddress(patientDto.getAddress());
        patient.setPassword(patientDto.getPassword());  // Save password (ideally hashed)

        patientRepository.save(patient);
    }
}
