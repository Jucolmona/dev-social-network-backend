package com.codefactory.dev_social_network.autenticacion.messaging;

import java.time.LocalDateTime;


public class SesionIniciadaEvent {

    private final Long usuarioId;
    private final String correoElectronico;
    private final String ipAddress;
    private final String userAgent;
    private final LocalDateTime ocurridoEn;

    public SesionIniciadaEvent(Long usuarioId, String correoElectronico, String ipAddress, String userAgent) {
        this.usuarioId = usuarioId;
        this.correoElectronico = correoElectronico;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.ocurridoEn = LocalDateTime.now();
    }

    public Long getUsuarioId() { return usuarioId; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public LocalDateTime getOcurridoEn() { return ocurridoEn; }
}