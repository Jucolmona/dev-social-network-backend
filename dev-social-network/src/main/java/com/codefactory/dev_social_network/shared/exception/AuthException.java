package com.codefactory.dev_social_network.shared.exception;

public class AuthException extends RuntimeException {
    private final AuthErrorCode errorCode;
    private final Object details;

    public AuthException(AuthErrorCode errorCode, String message, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public AuthErrorCode getErrorCode() { return errorCode; }
    public Object getDetails() { return details; }
}