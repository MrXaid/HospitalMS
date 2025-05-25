package com.example.hospital.converter;

import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.Doctor;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorConverter {

    public static DoctorDTO entityToDto(Doctor doctor) {
        if (doctor == null) return null;

        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setEmail(doctor.getEmail());
        dto.setPassword(doctor.getPassword());
        dto.setFullName(doctor.getFullName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setDescription(doctor.getDescription());
        dto.setEducation(doctor.getEducation());
        dto.setAvailableDays(doctor.getAvailableDays());
        dto.setStartTime(doctor.getStartTime());
        dto.setEndTime(doctor.getEndTime());

        if (doctor.getAppointments() != null) {
            List<Long> appointmentIds = doctor.getAppointments()
                    .stream()
                    .map(Appointment::getId)
                    .collect(Collectors.toList());
            dto.setAppointmentIds(appointmentIds);
        }

        return dto;
    }

    public static Doctor dtoToEntity(DoctorDTO dto) {
        if (dto == null) return null;

        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setFullName(dto.getFullName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(dto.getPassword());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setDescription(dto.getDescription());
        doctor.setEducation(dto.getEducation());
        doctor.setAvailableDays(dto.getAvailableDays());
        doctor.setStartTime(dto.getStartTime());
        doctor.setEndTime(dto.getEndTime());

        // Appointments need to be loaded separately based on IDs, so set null here
        doctor.setAppointments(null);

        return doctor;
    }
}
