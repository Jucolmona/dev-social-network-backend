package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;
import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import com.codefactory.dev_social_network.autenticacion.interfaces.AuthService;
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

    private static final int MAX_INTENTOS = 3;
    private static final long BLOQUEO_MINUTOS = 5;

    private final UsuarioQueryService usuarioQueryService;
    private final CredencialRepository credencialRepository;
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

        credencial.desbloquearSiVencio();

        if (credencial.estaBloqueada()) {
            throw new CuentaBloqueadaException(credencial.getBloqueadoHasta());
        }

        boolean credencialesValidas = credencial.getPasswordHash() != null
                && passwordEncoder.matches(request.getPassword(), credencial.getPasswordHash());

        if (!credencialesValidas) {
            credencial.registrarIntentoFallido(MAX_INTENTOS, BLOQUEO_MINUTOS);
            credencialRepository.save(credencial);

            if (credencial.estaBloqueada()) {
                authEventPublisher.publicarCuentaBloqueada(
                        usuarioId, email, credencial.getIntentosFallidos(), credencial.getBloqueadoHasta());
            }
            throw new CredencialesInvalidasException();
        }

        credencial.reiniciarIntentosFallidos();
        credencialRepository.save(credencial);

        String token = jwtProvider.generarAccessToken(usuarioId);
        authEventPublisher.publicarSesionIniciada(usuarioId, email, null, null);

        return new LoginResponseDTO(email, "Inicio de sesión exitoso", token);
    }
}