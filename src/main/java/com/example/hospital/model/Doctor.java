package com.example.hospital.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String specialization;
    private String description;   // e.g. "Expert in cardiology"
    private String education;     // e.g. "MBBS, MD - Cardiology"

    /**
     * A comma-separated list of available days (e.g., "Monday,Wednesday,Friday")
     */
    private String availableDays;

    private LocalTime startTime;
    private LocalTime endTime;

    @OneToMany(mappedBy = "doctor")
//    @JsonManagedReference
    private List<Appointment> appointments;
}
