package com.codefactory.dev_social_network.usuarios.entity;

import java.time.LocalDate;
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

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    protected Usuario() {
    }

    public Usuario(String email) {
        this.email = email;
        this.fechaRegistro = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
}