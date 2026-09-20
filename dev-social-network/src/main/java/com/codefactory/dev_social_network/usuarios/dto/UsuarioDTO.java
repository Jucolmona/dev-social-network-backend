package com.codefactory.dev_social_network.usuarios.dto;

import java.util.UUID;

public record UsuarioDTO(UUID id, String email, String contraseñaHash) {
}
