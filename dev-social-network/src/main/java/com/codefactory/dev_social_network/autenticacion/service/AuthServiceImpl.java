package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;
import com.codefactory.dev_social_network.autenticacion.entity.AccountLock;
import com.codefactory.dev_social_network.autenticacion.interfaces.AuthService;
import com.codefactory.dev_social_network.autenticacion.messaging.AuthEventPublisher;
import com.codefactory.dev_social_network.autenticacion.repository.AccountLockRepository;
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
    private final AccountLockRepository accountLockRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthEventPublisher authEventPublisher;

    @Override
    @Transactional
    public LoginResponseDTO iniciarSesion(LoginRequestDTO request) {
        UsuarioDTO usuario = usuarioQueryService.buscarPorEmail(request.getEmail())
                .orElseThrow(CredencialesInvalidasException::new);

        UUID usuarioId = usuario.getId();

        AccountLock lock = accountLockRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> new AccountLock(usuarioId));

        lock.desbloquearSiVencio();

        if (lock.estaBloqueada()) {
            throw new CuentaBloqueadaException(lock.getBloqueadoHasta());
        }

        boolean credencialesValidas = passwordEncoder.matches(
                request.getPassword(), usuario.getPasswordHash());

        if (!credencialesValidas) {
            lock.registrarIntentoFallido(MAX_INTENTOS, BLOQUEO_MINUTOS);
            accountLockRepository.save(lock);

            if (lock.estaBloqueada()) {
                authEventPublisher.publicarCuentaBloqueada(
                        usuarioId, usuario.getEmail(), lock.getIntentosFallidos(), lock.getBloqueadoHasta());
            }
            throw new CredencialesInvalidasException();
        }

        lock.reiniciarIntentosFallidos();
        accountLockRepository.save(lock);

        String token = jwtProvider.generarAccessToken(usuarioId);
        authEventPublisher.publicarSesionIniciada(usuarioId, usuario.getEmail(), null, null);

        return new LoginResponseDTO(usuario.getEmail(), "Inicio de sesión exitoso", token);
    }
}