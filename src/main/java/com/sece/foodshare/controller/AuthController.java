package com.sece.foodshare.controller;

import com.sece.foodshare.dto.LoginRequest;
import com.sece.foodshare.dto.LoginResponse;
import com.sece.foodshare.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {

        return service.login(
                request,
                session
        );
    }

    @PostMapping("/logout")
    public String logout(
            HttpSession session) {

        service.logout(session);

        return "Logged out successfully";
    }

    @GetMapping("/me")
    public LoginResponse me(
            HttpSession session) {

        return service.currentUser(session);
    }
}