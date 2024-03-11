package com.lobby.app.controller;

import com.lobby.app.model.auth.AuthResponse;
import com.lobby.app.model.auth.AuthService;
import com.lobby.app.model.auth.LoginRequest;
import com.lobby.app.model.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular port use the API
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
       return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
