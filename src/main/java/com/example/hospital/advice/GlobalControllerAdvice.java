package com.example.hospital.advice;

import com.example.hospital.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final SessionService sessionService;

    @Autowired
    public GlobalControllerAdvice(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @ModelAttribute
    public void addUserToModel(@CookieValue(value = "SESSIONID", required = false) String sessionId,
                               Model model) {
        if (sessionId != null) {
            Object user = sessionService.getSessionUser(sessionId);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
    }

}
