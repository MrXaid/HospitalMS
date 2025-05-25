package com.example.hospital.controller;

import com.example.hospital.converter.AppointmentConverter;
import com.example.hospital.dto.AppointmentDTO;
import com.example.hospital.dto.PatientDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.service.AppointmentService;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentMvcController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private SessionService sessionService;

    // ✅ Load form to book appointment (GET)
    @GetMapping("/form")
    public String showAppointmentForm(@RequestParam("doctorId") Long doctorId,
                                      @CookieValue("SESSIONID") String sessionId,
                                      Model model) {
        Object user = sessionService.getSessionUser(sessionId);
        if (!(user instanceof PatientDTO)) {
            return "redirect:/login";
        }

        PatientDTO patient = (PatientDTO) user;
        AppointmentDTO dto = new AppointmentDTO();
        dto.setDoctorId(doctorId);
        dto.setPatientId(patient.getId());

        model.addAttribute("appointment", dto);
        return "/appointment-form";  // You already have this HTML template
    }

    // ✅ Handle form submission (POST)
    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute("appointment") AppointmentDTO appointmentDTO,
                                  @CookieValue("SESSIONID") String sessionId) {

        Object user = sessionService.getSessionUser(sessionId);
        if (!(user instanceof PatientDTO)) {
            return "redirect:/login";
        }

        PatientDTO patientDTO = (PatientDTO) user;
        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId());
        Patient patient = patientService.getPatientById(patientDTO.getId());

        Appointment appointment = AppointmentConverter.dtoToEntity(appointmentDTO, patient, doctor);
        appointment.setStatus(AppointmentStatus.PENDING);

        appointmentService.createAppointment(appointment);

        return "redirect:/dashboard"; // back to dashboard after booking
    }
}
