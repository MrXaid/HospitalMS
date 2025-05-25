package com.example.hospital.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PatientDTO {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;

    // Just appointment IDs to avoid full object fetching and looping
    private List<Long> appointmentIds;
}
