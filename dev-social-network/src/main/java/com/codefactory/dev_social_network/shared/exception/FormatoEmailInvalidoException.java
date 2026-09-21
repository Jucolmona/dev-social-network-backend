package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class FormatoEmailInvalidoException extends BusinessException {
    public FormatoEmailInvalidoException(String email) {
        super(
            "FORMATO_EMAIL_INVALIDO",
            "El correo " + email + " no tiene un formato válido.",
            HttpStatus.BAD_REQUEST
        );
    }
}