package com.codefactory.dev_social_network.autenticacion.interfaces;

import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;

public interface CredencialBloqueoPolicy {
    boolean estaBloqueada(CredencialEntity credencial);
    void registrarIntentoFallido(CredencialEntity credencial);
    void reiniciarIntentosFallidos(CredencialEntity credencial);
    void desbloquearSiVencio(CredencialEntity credencial);
}