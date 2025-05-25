package com.example.hospital.dto;

import com.example.hospital.model.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDateTime dateTime;
    private AppointmentStatus status;

    // Only patient and doctor IDs (no full objects)
    private Long patientId;
    private Long doctorId;
}
