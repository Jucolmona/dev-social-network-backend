package com.codefactory.dev_social_network.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistroUsuarioRequest(
        @NotBlank(message = "El correo es obligatorio") String email,
        @NotBlank(message = "La contraseña es obligatoria") String contraseña) {
}