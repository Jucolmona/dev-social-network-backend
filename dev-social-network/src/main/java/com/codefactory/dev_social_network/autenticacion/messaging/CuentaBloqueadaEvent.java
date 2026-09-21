package com.codefactory.dev_social_network.autenticacion.messaging;

import java.time.LocalDateTime;
import java.util.UUID;

public class CuentaBloqueadaEvent {

    private final UUID usuarioId;
    private final String email;
    private final int intentosFallidos;
    private final LocalDateTime bloqueadoHasta;
    private final LocalDateTime ocurridoEn;

    public CuentaBloqueadaEvent(UUID usuarioId, String email,
                                 int intentosFallidos, LocalDateTime bloqueadoHasta) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.intentosFallidos = intentosFallidos;
        this.bloqueadoHasta = bloqueadoHasta;
        this.ocurridoEn = LocalDateTime.now();
    }

    public UUID getUsuarioId() { return usuarioId; }
    public String getEmail() { return email; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
}