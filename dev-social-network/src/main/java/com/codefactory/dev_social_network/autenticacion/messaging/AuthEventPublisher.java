package com.codefactory.dev_social_network.autenticacion.messaging;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuthEventPublisher {

    private final ApplicationEventPublisher publisher;

    public AuthEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publicarSesionIniciada(UUID usuarioId, String email, String ip, String userAgent) {
        publisher.publishEvent(new SesionIniciadaEvent(usuarioId, email, ip, userAgent));
    }

    public void publicarCuentaBloqueada(UUID usuarioId, String email, int intentosFallidos, LocalDateTime bloqueadoHasta) {
        publisher.publishEvent(new CuentaBloqueadaEvent(usuarioId, email, intentosFallidos, bloqueadoHasta));
    }
}