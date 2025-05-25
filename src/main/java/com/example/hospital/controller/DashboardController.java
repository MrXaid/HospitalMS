package com.example.hospital.controller;

import com.example.hospital.converter.AppointmentConverter;
import com.example.hospital.converter.DoctorConverter;
import com.example.hospital.dto.AppointmentDTO;
import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.dto.PatientDTO;
import com.example.hospital.service.AppointmentService;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final SessionService sessionService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @Autowired
    public DashboardController(SessionService sessionService,
                               DoctorService doctorService,
                               AppointmentService appointmentService) {
        this.sessionService = sessionService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@CookieValue(value = "SESSIONID", required = false) String sessionId,
                            Model model) {
        if (sessionId == null) {
            return "redirect:/";
        }

        Object user = sessionService.getSessionUser(sessionId);
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);

        if (user instanceof DoctorDTO doctorDTO) {
            List<AppointmentDTO> appointments = appointmentService
                    .getAppointmentsForDoctor(doctorDTO.getId())
                    .stream()
                    .map(AppointmentConverter::entityToDto)
                    .collect(Collectors.toList());

            model.addAttribute("appointments", appointments);
            return "doctor/dashboard";

        } else if (user instanceof PatientDTO patientDTO) {
            List<DoctorDTO> doctors = doctorService.getAllDoctors()
                    .stream()
                    .map(DoctorConverter::entityToDto)
                    .collect(Collectors.toList());

            List<AppointmentDTO> appointments = appointmentService
                    .getAppointmentsForPatient(patientDTO.getId())
                    .stream()
                    .map(AppointmentConverter::entityToDto)
                    .collect(Collectors.toList());

            model.addAttribute("doctors", doctors);
            model.addAttribute("appointments", appointments);
            return "patient/dashboard";

        } else {
            return "redirect:/logout";
        }
    }
}
