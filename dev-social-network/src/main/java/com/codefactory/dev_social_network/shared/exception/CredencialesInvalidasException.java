package com.codefactory.dev_social_network.shared.exception;

public class CredencialesInvalidasException extends AuthException {
    public CredencialesInvalidasException() {
        super(AuthErrorCode.CREDENCIALES_INVALIDAS, "Email o contraseña incorrectos", null);
    }
}