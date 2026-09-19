package com.codefactory.dev_social_network.autenticacion.dto;

public class LoginResponseDTO {

    private String correoElectronico;
    private String mensaje;

    public LoginResponseDTO(String correoElectronico, String mensaje) {
        this.correoElectronico = correoElectronico;
        this.mensaje = mensaje;
    }

    public String getCorreoElectronico() { return correoElectronico; }
    public String getMensaje() { return mensaje; }
}