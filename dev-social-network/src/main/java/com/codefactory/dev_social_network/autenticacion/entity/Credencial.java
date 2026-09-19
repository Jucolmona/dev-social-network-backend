package com.codefactory.dev_social_network.autenticacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credenciales")
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String correoElectronico;

    @Column(nullable = false)
    private String contrasenaHash;

    @Column(nullable = false)
    private int intentosFallidos = 0;

    
    private LocalDateTime bloqueadoHasta;

    protected Credencial() {
        
    }

    public Credencial(String correoElectronico, String contrasenaHash) {
        this.correoElectronico = correoElectronico;
        this.contrasenaHash = contrasenaHash;
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

    public Long getId() { return id; }

    public String getCorreoElectronico() { return correoElectronico; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public int getIntentosFallidos() { return intentosFallidos; }

    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
}