package com.codefactory.dev_social_network.usuarios.controller;

import com.codefactory.dev_social_network.usuarios.dto.RegistroUsuarioRequest;
import com.codefactory.dev_social_network.usuarios.dto.RegistroUsuarioResponse;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrarUsuarioUseCase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    public UsuarioController(RegistrarUsuarioUseCase registrarUsuarioUseCase) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<RegistroUsuarioResponse> registrar(@RequestBody RegistroUsuarioRequest request) {
        UUID id = registrarUsuarioUseCase.registrar(request.email(), request.contraseña());
        RegistroUsuarioResponse response = new RegistroUsuarioResponse(id, "Cuenta creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}