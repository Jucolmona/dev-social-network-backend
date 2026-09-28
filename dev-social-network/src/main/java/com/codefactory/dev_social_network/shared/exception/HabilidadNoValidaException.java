package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class HabilidadNoValidaException extends BusinessException {
    public HabilidadNoValidaException(String nombreHabilidad) {
        super(
            "HABILIDAD_NO_VALIDA",
            "La habilidad \"" + nombreHabilidad + "\" no es válida. Debe estar dentro de los Roles, Lenguajes de programación o Frameworks permitidos.",
            HttpStatus.BAD_REQUEST
        );
    }
}