package com.example.hospital.controller;

import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorAuthController {

    private final DoctorService doctorService;
    private final SessionService sessionService;

    @Autowired
    public DoctorAuthController(DoctorService doctorService, SessionService sessionService) {
        this.doctorService = doctorService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String showDoctorLogin() {
        return "doctor/login"; // View name for login page
    }

    @PostMapping("/login")
    public String doctorLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            DoctorDTO doctor = doctorService.authenticate(email, password);
            String sessionId = sessionService.createSession(doctor);

            Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            // Optionally uncomment for HTTPS only:
            // sessionCookie.setSecure(true);
            response.addCookie(sessionCookie);

            return "redirect:/dashboard";
        } catch (Exception e) { // Replace Exception with your custom AuthenticationException
            return "redirect:/doctor/login?error";
        }
    }

    @GetMapping("/signup")
    public String showDoctorSignup() {
        return "doctor/signup"; // View name for signup page
    }

    @PostMapping("/signup")
    public String doctorSignup(@Valid DoctorDTO doctorDto, BindingResult result) {
        if (result.hasErrors()) {
            return "doctor/signup"; // Return to signup form if validation fails
        }
        doctorService.registerDoctor(doctorDto);
        return "redirect:/doctor/login?success";
    }
}
