package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class EnlaceInvalidoException extends BusinessException {
    public EnlaceInvalidoException(String mensaje) {
        super("ENLACE_INVALIDO", mensaje, HttpStatus.BAD_REQUEST);
    }
}