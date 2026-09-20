package com.codefactory.dev_social_network.usuarios.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "credenciales")
public class Credencial {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "intentos_fallidos", nullable = false)
    private int intentosFallidos;

    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;

    protected Credencial() {
    }

    public Credencial(Usuario usuario, String passwordHash) {
        this.usuario = usuario;
        this.tipo = "LOCAL";
        this.passwordHash = passwordHash;
        this.intentosFallidos = 0;
    }

    public UUID getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getTipo() { return tipo; }
    public String getPasswordHash() { return passwordHash; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
}