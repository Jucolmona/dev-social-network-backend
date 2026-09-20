package com.codefactory.dev_social_network.autenticacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account_locks")
public class AccountLock {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private UUID usuarioId;

    @Column(nullable = false)
    private int intentosFallidos = 0;

    private LocalDateTime bloqueadoHasta;

    protected AccountLock() {}

    public AccountLock(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean estaBloqueada() {
        return bloqueadoHasta != null && bloqueadoHasta.isAfter(LocalDateTime.now());
    }

    public void registrarIntentoFallido(int maximoIntentos, long minutosBloqueo) {
        this.intentosFallidos++;
        if (this.intentosFallidos >= maximoIntentos) {
            this.bloqueadoHasta = LocalDateTime.now().plusMinutes(minutosBloqueo);
        }
    }

    public void reiniciarIntentosFallidos() {
        this.intentosFallidos = 0;
        this.bloqueadoHasta = null;
    }

    public void desbloquearSiVencio() {
        if (bloqueadoHasta != null && !bloqueadoHasta.isAfter(LocalDateTime.now())) {
            reiniciarIntentosFallidos();
        }
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
}