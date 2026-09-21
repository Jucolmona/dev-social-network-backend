package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode {
    CREDENCIALES_INVALIDAS(HttpStatus.UNAUTHORIZED),
    CUENTA_BLOQUEADA(HttpStatus.LOCKED),
    PASSWORD_NO_CUMPLE_POLITICA(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;

    AuthErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}