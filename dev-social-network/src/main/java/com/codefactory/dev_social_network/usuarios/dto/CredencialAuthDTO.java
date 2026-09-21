package com.codefactory.dev_social_network.usuarios.dto;

import java.util.UUID;

public record CredencialAuthDTO(UUID usuarioId, String email, String contrasenaHash) {
}