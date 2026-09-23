package com.codefactory.dev_social_network.usuarios.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.dev_social_network.usuarios.dto.RegistroUsuarioRequest;
import com.codefactory.dev_social_network.usuarios.dto.RegistroUsuarioResponse;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileEditionRequestDTO;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileResponseDTO;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrarUsuarioUseCase;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final UserProfileService userProfileService;

    public UsuarioController(RegistrarUsuarioUseCase registrarUsuarioUseCase,
                             UserProfileService userProfileService) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<RegistroUsuarioResponse> registrar(@Valid @RequestBody RegistroUsuarioRequest request) {
        UUID id = registrarUsuarioUseCase.registrar(
                request.email(), request.nombre(), request.apellido(), request.contraseña());
        RegistroUsuarioResponse response = new RegistroUsuarioResponse(id, "Cuenta creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}/perfil")
    public ResponseEntity<UserProfileResponseDTO> actualizarPerfil(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileEditionRequestDTO request) {
        UserProfileResponseDTO response = userProfileService.updateUserProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/perfil")
    public ResponseEntity<UserProfileResponseDTO> obtenerPerfil(@PathVariable UUID userId) {
        UserProfileResponseDTO response = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }
}