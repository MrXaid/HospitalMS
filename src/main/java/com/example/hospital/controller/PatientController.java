package com.example.hospital.controller;

import com.example.hospital.converter.PatientConverter;
import com.example.hospital.dto.PatientDTO;
import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public PatientDTO createPatient(@RequestBody PatientDTO patientDTO) {
        Patient patientEntity = PatientConverter.dtoToEntity(patientDTO);
        Patient savedPatient = patientService.createPatient(patientEntity);
        return PatientConverter.entityToDto(savedPatient);
    }

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return patients.stream()
                .map(PatientConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PatientDTO getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return PatientConverter.entityToDto(patient);
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        Patient updatedPatient = patientService.updatePatient(id, PatientConverter.dtoToEntity(patientDTO));
        return PatientConverter.entityToDto(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
