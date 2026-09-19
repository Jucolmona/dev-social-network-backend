package com.codefactory.dev_social_network.autenticacion.messaging;

import java.time.LocalDateTime;


public class CuentaBloqueadaEvent {

    private final Long usuarioId;
    private final String correoElectronico;
    private final int intentosFallidos;
    private final LocalDateTime bloqueadoHasta;
    private final LocalDateTime ocurridoEn;

    public CuentaBloqueadaEvent(Long usuarioId, String correoElectronico,
                                 int intentosFallidos, LocalDateTime bloqueadoHasta) {
        this.usuarioId = usuarioId;
        this.correoElectronico = correoElectronico;
        this.intentosFallidos = intentosFallidos;
        this.bloqueadoHasta = bloqueadoHasta;
        this.ocurridoEn = LocalDateTime.now();
    }

    public Long getUsuarioId() { return usuarioId; }
    public String getCorreoElectronico() { return correoElectronico; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
    public LocalDateTime getOcurridoEn() { return ocurridoEn; }
}