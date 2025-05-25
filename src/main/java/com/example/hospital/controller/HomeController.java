package com.example.hospital.controller;

import com.example.hospital.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final SessionService sessionService;
    private final RestTemplate restTemplate;


    @Autowired
    public HomeController(SessionService sessionService, RestTemplate restTemplate) {
        this.sessionService = sessionService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String home(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        if (sessionId != null && sessionService.getSessionUser(sessionId) != null) {
            return "redirect:/dashboard";
        }
        return "home";  // Basic homepage with links to /patient/login and /doctor/login
    }

    @GetMapping("/chatwithai")
    public String showChatPage(Model model) {
        // Initialize empty chat
        model.addAttribute("userMessage", "");
        model.addAttribute("aiResponse", "");
        return "chat";
    }
    @PostMapping("/chatwithai")
    public String handleChatRequest(
            @RequestParam String userMessage,
            Model model) {

        try {
            // Call Flask AI service
            String flaskUrl = "http://localhost:5000/chat";
            String aiResponse = restTemplate.postForObject(
                    flaskUrl,
                    Map.of("message", userMessage),
                    String.class
            );

            model.addAttribute("userMessage", userMessage);
            model.addAttribute("aiResponse", aiResponse);

        } catch (Exception e) {
            model.addAttribute("error", "AI service is currently unavailable");
            model.addAttribute("userMessage", userMessage); // Preserve input
        }

        return "chat";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "SESSIONID", required = false) String sessionId,
                         HttpSession session,
                         jakarta.servlet.http.HttpServletResponse response) {
        if (sessionId != null) {
            sessionService.invalidateSession(sessionId);  // remove session from map
        }
        session.invalidate();  // invalidate HttpSession just in case

        // Remove the cookie on client side
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("SESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");  // Important to match cookie path!
        response.addCookie(cookie);

        return "redirect:/";  // Redirect to home/login page
    }
    @GetMapping("/pharmacy")
    public String pharmacy(@CookieValue(value = "SESSIONID", required = false) String sessionId, Model model) {
        // Check if user logged in
        var user = sessionId != null ? sessionService.getSessionUser(sessionId) : null;
        if (user == null) {
            return "redirect:/"; // redirect to home if not logged in
        }

        // Hardcoded list of products
        List<Product> products = Arrays.asList(
                new Product("Paracetamol", "Pain reliever", 5.0),
                new Product("Aspirin", "Blood thinner", 7.5),
                new Product("Cough Syrup", "Relieves cough", 8.0)
        );

        model.addAttribute("products", products);
        return "pharmacy";
    }

    // Inner static class to represent product for now
    public static class Product {
        private final String name;
        private final String description;
        private final double price;

        public Product(String name, String description, double price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
    }

}
