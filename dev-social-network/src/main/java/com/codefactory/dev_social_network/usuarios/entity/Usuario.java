package com.codefactory.dev_social_network.usuarios.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {



    @Id 
    @GeneratedValue 
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column (name="contrasena_hash", nullable = false)
    private String contrasenaHash;

    protected Usuario() {
    }

    public Usuario(String email, String contrasenaHash) {
        this.email = email;
        this.contrasenaHash = contrasenaHash;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    
    
    
}
