package com.codefactory.dev_social_network.autenticacion.messaging;

import java.time.LocalDateTime;
import java.util.UUID;

public class SesionIniciadaEvent {

    private final UUID usuarioId;
    private final String email;
    private final String ipAddress;
    private final String userAgent;
    private final LocalDateTime ocurridoEn;

    public SesionIniciadaEvent(UUID usuarioId, String email, String ipAddress, String userAgent) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.ocurridoEn = LocalDateTime.now();
    }

    public UUID getUsuarioId() { return usuarioId; }
    public String getEmail() { return email; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public LocalDateTime getOcurridoEn() { return ocurridoEn; }
}