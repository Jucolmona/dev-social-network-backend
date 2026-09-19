package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.shared.exception.AuthErrorCode;
import com.codefactory.dev_social_network.shared.exception.AuthException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordPolicyValidator {
    private static final Pattern POLICY = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-]).{8,}$");

    public void validar(String password) {
        if (password == null || !POLICY.matcher(password).matches()) {
            throw new AuthException(AuthErrorCode.PASSWORD_NO_CUMPLE_POLITICA,
                "La contraseña no cumple la política de seguridad",
                List.of("Mínimo 8 caracteres, mayúscula, minúscula, dígito y símbolo"));
        }
    }
}