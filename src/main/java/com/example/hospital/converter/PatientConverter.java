package com.example.hospital.converter;

import com.example.hospital.dto.PatientDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class PatientConverter {

    public static PatientDTO entityToDto(Patient patient) {
        if (patient == null) return null;

        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setFullName(patient.getFullName());
        dto.setEmail(patient.getEmail());
        dto.setPassword(patient.getPassword());

        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());

        if (patient.getAppointments() != null) {
            List<Long> appointmentIds = patient.getAppointments()
                    .stream()
                    .map(Appointment::getId)
                    .collect(Collectors.toList());
            dto.setAppointmentIds(appointmentIds);
        }

        return dto;
    }

    public static Patient dtoToEntity(PatientDTO dto) {
        if (dto == null) return null;

        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());

        // For appointments, you can load Appointment entities from DB using IDs in the service layer.
        // So here we leave appointments null or empty.
        patient.setAppointments(null);

        return patient;
    }
}
