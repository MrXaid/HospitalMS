package com.example.hospital.converter;

import com.example.hospital.dto.AppointmentDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;

public class AppointmentConverter {

    public static AppointmentDTO entityToDto(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDateTime(appointment.getDateTime());
        dto.setStatus(appointment.getStatus());

        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
        }
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
        }

        return dto;
    }

    public static Appointment dtoToEntity(AppointmentDTO dto, Patient patient, Doctor doctor) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setDateTime(dto.getDateTime());
        appointment.setStatus(dto.getStatus());

        // Set patient and doctor entities (loaded separately by service layer)
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        return appointment;
    }
}
