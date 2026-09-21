package com.codefactory.dev_social_network.autenticacion.interfaces;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO iniciarSesion(LoginRequestDTO request);
}