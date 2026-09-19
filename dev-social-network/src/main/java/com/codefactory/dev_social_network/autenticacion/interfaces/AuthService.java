package com.codefactory.dev_social_network.autenticacion.interfaces;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request, String ipAddress, String userAgent);

    void logout(String accessToken);

    void revocarTodosLosTokens(Long usuarioId);

    boolean estaCuentaBloqueada(Long usuarioId);
}
