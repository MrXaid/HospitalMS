package com.example.hospital.service;

import com.example.hospital.converter.DoctorConverter;
import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.model.Doctor;
import com.example.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        existingDoctor.setFullName(updatedDoctor.getFullName());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setPassword(updatedDoctor.getPassword());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setDescription(updatedDoctor.getDescription());
        existingDoctor.setEducation(updatedDoctor.getEducation());
        existingDoctor.setAvailableDays(updatedDoctor.getAvailableDays());
        existingDoctor.setStartTime(updatedDoctor.getStartTime());
        existingDoctor.setEndTime(updatedDoctor.getEndTime());

        return doctorRepository.save(existingDoctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // New authenticate method
    public DoctorDTO authenticate(String email, String password) {
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(email);
        if (optionalDoctor.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        Doctor doctor = optionalDoctor.get();

        // You should hash & salt passwords in real applications; here is a simple example
        if (!doctor.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        return DoctorConverter.entityToDto(doctor);
    }

    // Optional: registerDoctor method that accepts DoctorDTO
    public DoctorDTO registerDoctor(DoctorDTO doctorDTO) {
        // Convert DTO to entity
        Doctor doctor = DoctorConverter.dtoToEntity(doctorDTO);

        // Here you should hash the password before saving (important for security!)
        // doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));

        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorConverter.entityToDto(savedDoctor);
    }
}
