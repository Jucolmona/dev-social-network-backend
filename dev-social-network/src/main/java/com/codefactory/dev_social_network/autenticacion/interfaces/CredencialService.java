package com.codefactory.dev_social_network.autenticacion.interfaces;

import java.util.UUID;

public interface CredencialService {
    void crearCredencialLocal(UUID usuarioId, String passwordHash);
}