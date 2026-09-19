package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;
import com.codefactory.dev_social_network.autenticacion.entity.Credencial;
import com.codefactory.dev_social_network.autenticacion.interfaces.AuthService;
import com.codefactory.dev_social_network.autenticacion.repository.CredencialRepository;
import com.codefactory.dev_social_network.shared.exception.CredencialesInvalidasException;
import com.codefactory.dev_social_network.shared.exception.CuentaBloqueadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int MAX_INTENTOS = 3;
    private static final long BLOQUEO_MINUTOS = 5;

    private final CredencialRepository credencialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public LoginResponseDTO iniciarSesion(LoginRequestDTO request) {
        Credencial credencial = credencialRepository.findByCorreoElectronico(request.getCorreoElectronico())
                .orElseThrow(CredencialesInvalidasException::new);

        credencial.desbloquearSiVencio();

        if (credencial.estaBloqueada()) {
            throw new CuentaBloqueadaException(credencial.getBloqueadoHasta());
        }

        boolean credencialesValidas = passwordEncoder.matches(
                request.getContrasena(), credencial.getContrasenaHash());

        if (!credencialesValidas) {
            credencial.registrarIntentoFallido(MAX_INTENTOS, BLOQUEO_MINUTOS);
            credencialRepository.save(credencial);
            throw new CredencialesInvalidasException();
        }

        credencial.reiniciarIntentosFallidos();
        credencialRepository.save(credencial);

        String token = jwtProvider.generarAccessToken(credencial.getId());

        return new LoginResponseDTO(credencial.getCorreoElectronico(), "Inicio de sesión exitoso");
    }
}