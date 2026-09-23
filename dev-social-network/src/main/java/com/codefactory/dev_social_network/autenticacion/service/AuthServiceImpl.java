package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;
import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import com.codefactory.dev_social_network.autenticacion.interfaces.AuthService;
import com.codefactory.dev_social_network.autenticacion.interfaces.CredencialBloqueoPolicy;
import com.codefactory.dev_social_network.autenticacion.messaging.AuthEventPublisher;
import com.codefactory.dev_social_network.autenticacion.repository.CredencialRepository;
import com.codefactory.dev_social_network.shared.exception.CredencialesInvalidasException;
import com.codefactory.dev_social_network.shared.exception.CuentaBloqueadaException;
import com.codefactory.dev_social_network.usuarios.dto.UsuarioDTO;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioQueryService usuarioQueryService;
    private final CredencialRepository credencialRepository;
    private final CredencialBloqueoPolicy credencialBloqueoPolicy;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthEventPublisher authEventPublisher;

    @Override
    @Transactional(noRollbackFor = {
            CredencialesInvalidasException.class,
            CuentaBloqueadaException.class
    })
    public LoginResponseDTO iniciarSesion(LoginRequestDTO request) {
        UsuarioDTO usuario = usuarioQueryService.buscarPorEmail(request.getEmail())
                .orElseThrow(CredencialesInvalidasException::new);

        UUID usuarioId = usuario.id();
        String email = usuario.email();

        CredencialEntity credencial = credencialRepository.findByUsuarioId(usuarioId)
                .orElseThrow(CredencialesInvalidasException::new);

        credencialBloqueoPolicy.desbloquearSiVencio(credencial);

        if (credencialBloqueoPolicy.estaBloqueada(credencial)) {
            throw new CuentaBloqueadaException(credencial.getBloqueadoHasta());
        }

        boolean credencialesValidas = credencial.getPasswordHash() != null
                && passwordEncoder.matches(request.getPassword(), credencial.getPasswordHash());

        if (!credencialesValidas) {
            credencialBloqueoPolicy.registrarIntentoFallido(credencial);
            credencialRepository.save(credencial);

            if (credencialBloqueoPolicy.estaBloqueada(credencial)) {
                authEventPublisher.publicarCuentaBloqueada(
                        usuarioId, email, credencial.getIntentosFallidos(), credencial.getBloqueadoHasta());
            }
            throw new CredencialesInvalidasException();
        }

        credencialBloqueoPolicy.reiniciarIntentosFallidos(credencial);
        credencialRepository.save(credencial);

        String token = jwtProvider.generarAccessToken(usuarioId);
        authEventPublisher.publicarSesionIniciada(usuarioId, email, null, null);

        return new LoginResponseDTO(email, "Inicio de sesión exitoso", token);
    }
}