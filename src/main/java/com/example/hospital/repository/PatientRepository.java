package com.example.hospital.repository;

import com.example.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Add custom queries if needed later
    Optional<Patient> findByEmail(String email);
}
