package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class NivelExperienciaInvalidoException extends BusinessException {
    public NivelExperienciaInvalidoException(String nivel) {
        super(
            "NIVEL_EXPERIENCIA_INVALIDO",
            "El nivel de experiencia \"" + nivel + "\" no es válido. Debe ser uno de: ENTRY, JUNIOR, MID, SENIOR.",
            HttpStatus.BAD_REQUEST
        );
    }
}