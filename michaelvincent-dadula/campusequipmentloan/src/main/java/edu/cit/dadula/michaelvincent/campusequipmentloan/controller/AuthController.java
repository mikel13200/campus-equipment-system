package edu.cit.dadula.michaelvincent.campusequipmentloan.controller;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.User;
import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Student;
import edu.cit.dadula.michaelvincent.campusequipmentloan.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student student) {
        try {
            Student savedStudent = authService.register(student);
            return ResponseEntity.ok(savedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        boolean success = authService.login(username, password);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
    }
}
