package com.codefactory.dev_social_network.autenticacion.dto;

public class LoginResponseDTO {

    private String email;
    private String mensaje;
    private String token;

    public LoginResponseDTO(String email, String mensaje, String token) {
        this.email = email;
        this.mensaje = mensaje;
        this.token = token;
    }

    public String getEmail() { return email; }
    public String getMensaje() { return mensaje; }
    public String getToken() { return token; }
}