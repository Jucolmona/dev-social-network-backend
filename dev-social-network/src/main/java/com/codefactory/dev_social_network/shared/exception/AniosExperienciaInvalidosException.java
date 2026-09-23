package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class AniosExperienciaInvalidosException extends BusinessException {
    public AniosExperienciaInvalidosException(int anios) {
        super(
            "ANIOS_EXPERIENCIA_INVALIDOS",
            "Los años de experiencia deben estar en el rango de 0 a 20 años. Valor recibido: " + anios,
            HttpStatus.BAD_REQUEST
        );
    }
}