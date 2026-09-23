package com.codefactory.dev_social_network.shared.exception;

public class AuthException extends BusinessException {
    private final Object details;

    public AuthException(AuthErrorCode errorCode, String message, Object details) {
        super(errorCode.name(), message, errorCode.getStatus());
        this.details = details;
    }

    public Object getDetails() { return details; }
}