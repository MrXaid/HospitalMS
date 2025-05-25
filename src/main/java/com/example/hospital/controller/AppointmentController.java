package com.example.hospital.controller;

import com.example.hospital.converter.AppointmentConverter;
import com.example.hospital.service.SessionService;
import com.example.hospital.dto.AppointmentDTO;
import com.example.hospital.dto.PatientDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.AppointmentStatus;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.service.AppointmentService;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public AppointmentDTO createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Patient patient = patientService.getPatientById(appointmentDTO.getPatientId());
        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId());
        Appointment appointment = AppointmentConverter.dtoToEntity(appointmentDTO, patient, doctor);
        Appointment saved = appointmentService.createAppointment(appointment);
        return AppointmentConverter.entityToDto(saved);
    }
    @GetMapping("/form")
    public String showAppointmentForm(@RequestParam("doctorId") Long doctorId,
                                      @CookieValue("SESSION_ID") String sessionId,
                                      Model model) {
        // ✅ Use the instance, not the class
        Object user = sessionService.getSessionUser(sessionId);

        if (!(user instanceof PatientDTO)) {
            return "redirect:/login"; // or handle unauthorized
        }

        PatientDTO patient = (PatientDTO) user;

        AppointmentDTO dto = new AppointmentDTO();
        dto.setDoctorId(doctorId);
        dto.setPatientId(patient.getId());
        model.addAttribute("appointment", dto);

        return "appointment-form"; // Return the view name for Thymeleaf (not JSON!)
    }

    @PostMapping("/book") // optional: rename for clarity
    public AppointmentDTO bookAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Patient patient = patientService.getPatientById(appointmentDTO.getPatientId());
        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId());
        Appointment appointment = AppointmentConverter.dtoToEntity(appointmentDTO, patient, doctor);

        appointment.setStatus(AppointmentStatus.PENDING); // override any client input

        Appointment saved = appointmentService.createAppointment(appointment);
        return AppointmentConverter.entityToDto(saved);
    }

    @PatchMapping("/{id}/status")
    public AppointmentDTO updateAppointmentStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        Appointment updated = appointmentService.updateAppointmentStatus(id, status);
        return AppointmentConverter.entityToDto(updated);
    }



    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentService.getAllAppointments()
                .stream()
                .map(AppointmentConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AppointmentDTO getAppointmentById(@PathVariable Long id) {
        return AppointmentConverter.entityToDto(appointmentService.getAppointmentById(id));
    }

    @PutMapping("/{id}")
    public AppointmentDTO updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        Patient patient = patientService.getPatientById(appointmentDTO.getPatientId());
        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId());
        Appointment appointment = AppointmentConverter.dtoToEntity(appointmentDTO, patient, doctor);
        Appointment updated = appointmentService.updateAppointment(id, appointment);
        return AppointmentConverter.entityToDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}
