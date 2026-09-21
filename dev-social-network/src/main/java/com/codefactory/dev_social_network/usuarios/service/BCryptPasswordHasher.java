package com.codefactory.dev_social_network.usuarios.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.usuarios.interfaces.PasswordHasher;

@Component
public class BCryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hashear(String contrasenaPlana) {
        return encoder.encode(contrasenaPlana);
    }
}