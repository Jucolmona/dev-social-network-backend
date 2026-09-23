package com.codefactory.dev_social_network.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistroUsuarioRequest(
        @NotBlank(message = "El correo es obligatorio") String email,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        @NotBlank(message = "La contraseña es obligatoria") String contraseña) {
}