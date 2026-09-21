package com.codefactory.dev_social_network.autenticacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "credencial")
public class CredencialEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private UUID usuarioId;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "proveedor_id_externo")
    private String proveedorIdExterno;

    @Column(name = "intentos_fallidos", nullable = false)
    private int intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;

    protected CredencialEntity() {}

    public CredencialEntity(UUID usuarioId, String tipo, String passwordHash, String proveedorIdExterno) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.passwordHash = passwordHash;
        this.proveedorIdExterno = proveedorIdExterno;
        this.intentosFallidos = 0;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getTipo() { return tipo; }
    public String getPasswordHash() { return passwordHash; }
    public String getProveedorIdExterno() { return proveedorIdExterno; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }

    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }
}