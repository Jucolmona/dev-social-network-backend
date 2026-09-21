package com.codefactory.dev_social_network.autenticacion.controller;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;
import com.codefactory.dev_social_network.autenticacion.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO respuesta = authService.iniciarSesion(request);
        return ResponseEntity.ok(respuesta);
    }
}