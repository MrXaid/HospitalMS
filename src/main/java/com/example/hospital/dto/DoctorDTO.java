package com.example.hospital.dto;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class DoctorDTO {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String specialization;
    private String description;
    private String education;

    // A comma-separated list like in your entity
    private String availableDays;
    private LocalTime startTime;
    private LocalTime endTime;

    // Only appointment IDs to avoid cycles
    private List<Long> appointmentIds;
}
