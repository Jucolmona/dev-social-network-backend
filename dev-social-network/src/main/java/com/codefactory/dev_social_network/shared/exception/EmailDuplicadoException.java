// EmailDuplicadoException.java
package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class EmailDuplicadoException extends BusinessException {
    public EmailDuplicadoException(String email) {
        super(
            "EMAIL_DUPLICADO",
            "El correo " + email + " ya se encuentra registrado.",
            HttpStatus.CONFLICT
        );
    }
}