package com.codefactory.dev_social_network.shared.exception;

import org.springframework.http.HttpStatus;

public class PasswordInseguraException extends BusinessException {
    public PasswordInseguraException(String mensaje) {
        super("PASSWORD_INSEGURA", mensaje, HttpStatus.BAD_REQUEST);
    }
}