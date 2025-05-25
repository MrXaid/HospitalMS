package com.example.hospital.controller;

import com.example.hospital.dto.PatientDTO;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientAuthController {

    private final PatientService patientService;
    private final SessionService sessionService;

    @Autowired
    public PatientAuthController(PatientService patientService, SessionService sessionService) {
        this.patientService = patientService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String showPatientLogin() {
        return "patient/login";  // Thymeleaf or JSP view name
    }

    @PostMapping("/login")
    public String patientLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            PatientDTO patient = patientService.authenticate(email, password);

            System.out.println("Authenticated PatientDTO: " + patient);  // Add this line

            String sessionId = sessionService.createSession(patient);

            Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            response.addCookie(sessionCookie);

            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();  // Also log the exception here
            return "redirect:/patient/login?error";
        }
    }


    @GetMapping("/signup")
    public String showPatientSignup() {
        return "patient/signup";  // View for signup form
    }

    @PostMapping("/signup")
    public String patientSignup(@Valid PatientDTO patientDto, BindingResult result) {
        if (result.hasErrors()) {
            return "patient/signup";
        }
        patientService.registerPatient(patientDto);
        return "redirect:/patient/login?success";
    }
}
