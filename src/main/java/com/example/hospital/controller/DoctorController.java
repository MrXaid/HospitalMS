package com.example.hospital.controller;

import com.example.hospital.converter.DoctorConverter;
import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.model.Doctor;
import com.example.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public DoctorDTO createDoctor(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctorEntity = DoctorConverter.dtoToEntity(doctorDTO);
        Doctor savedDoctor = doctorService.createDoctor(doctorEntity);
        return DoctorConverter.entityToDto(savedDoctor);
    }

    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return doctors.stream()
                .map(DoctorConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DoctorDTO getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return DoctorConverter.entityToDto(doctor);
    }

    @PutMapping("/{id}")
    public DoctorDTO updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        Doctor updatedDoctor = doctorService.updateDoctor(id, DoctorConverter.dtoToEntity(doctorDTO));
        return DoctorConverter.entityToDto(updatedDoctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
