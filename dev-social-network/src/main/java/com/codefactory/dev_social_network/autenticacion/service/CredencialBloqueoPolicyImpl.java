package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import com.codefactory.dev_social_network.autenticacion.interfaces.CredencialBloqueoPolicy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CredencialBloqueoPolicyImpl implements CredencialBloqueoPolicy {

    private static final int MAX_INTENTOS = 3;
    private static final long BLOQUEO_MINUTOS = 5;

    @Override
    public boolean estaBloqueada(CredencialEntity credencial) {
        LocalDateTime bloqueadoHasta = credencial.getBloqueadoHasta();
        return bloqueadoHasta != null && bloqueadoHasta.isAfter(LocalDateTime.now());
    }

    @Override
    public void registrarIntentoFallido(CredencialEntity credencial) {
        int intentos = credencial.getIntentosFallidos() + 1;
        credencial.setIntentosFallidos(intentos);
        if (intentos >= MAX_INTENTOS) {
            credencial.setBloqueadoHasta(LocalDateTime.now().plusMinutes(BLOQUEO_MINUTOS));
        }
    }

    @Override
    public void reiniciarIntentosFallidos(CredencialEntity credencial) {
        credencial.setIntentosFallidos(0);
        credencial.setBloqueadoHasta(null);
    }

    @Override
    public void desbloquearSiVencio(CredencialEntity credencial) {
        LocalDateTime bloqueadoHasta = credencial.getBloqueadoHasta();
        if (bloqueadoHasta != null && !bloqueadoHasta.isAfter(LocalDateTime.now())) {
            reiniciarIntentosFallidos(credencial);
        }
    }
}