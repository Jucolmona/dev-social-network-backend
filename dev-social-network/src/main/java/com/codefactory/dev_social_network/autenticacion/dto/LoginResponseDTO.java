package com.codefactory.dev_social_network.autenticacion.dto;

public class LoginResponseDTO {

    private String correoElectronico;
    private String mensaje;
    private String token;

    public LoginResponseDTO(String correoElectronico, String mensaje, String token) {
        this.correoElectronico = correoElectronico;
        this.mensaje = mensaje;
        this.token = token;
    }

    public String getCorreoElectronico() { return correoElectronico; }
    public String getMensaje() { return mensaje; }
    public String getToken() { return token; }
}