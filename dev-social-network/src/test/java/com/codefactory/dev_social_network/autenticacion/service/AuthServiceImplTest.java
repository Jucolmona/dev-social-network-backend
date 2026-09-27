package com.codefactory.dev_social_network.autenticacion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codefactory.dev_social_network.autenticacion.dto.LoginRequestDTO;
import com.codefactory.dev_social_network.autenticacion.dto.LoginResponseDTO;
import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import com.codefactory.dev_social_network.autenticacion.interfaces.CredencialBloqueoPolicy;
import com.codefactory.dev_social_network.autenticacion.messaging.AuthEventPublisher;
import com.codefactory.dev_social_network.autenticacion.repository.CredencialRepository;
import com.codefactory.dev_social_network.shared.exception.CredencialesInvalidasException;
import com.codefactory.dev_social_network.shared.exception.CuentaBloqueadaException;
import com.codefactory.dev_social_network.usuarios.dto.UsuarioDTO;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioQueryService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static final String EMAIL = "ana@correo.com";
    private static final String PASSWORD = "Secreto1!";

    @Mock
    private UsuarioQueryService usuarioQueryService;
    @Mock
    private CredencialRepository credencialRepository;
    @Mock
    private CredencialBloqueoPolicy credencialBloqueoPolicy;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private AuthEventPublisher authEventPublisher;

    private AuthServiceImpl service;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(usuarioQueryService, credencialRepository, credencialBloqueoPolicy,
                passwordEncoder, jwtProvider, authEventPublisher);
        usuarioId = UUID.randomUUID();
    }

    private CredencialEntity credencial(String hash) {
        return new CredencialEntity(usuarioId, "LOCAL", hash, null);
    }

    private void usuarioExiste() {
        when(usuarioQueryService.buscarPorEmail(EMAIL)).thenReturn(Optional.of(new UsuarioDTO(usuarioId, EMAIL)));
    }

    @Test
    @DisplayName("inicia sesion y devuelve el token cuando las credenciales son validas")
    void iniciaSesionExitosa() {
        usuarioExiste();
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(credencial("hash")));
        when(passwordEncoder.matches(PASSWORD, "hash")).thenReturn(true);
        when(jwtProvider.generarAccessToken(usuarioId)).thenReturn("jwt-firmado");

        LoginResponseDTO respuesta = service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD));

        assertThat(respuesta.getEmail()).isEqualTo(EMAIL);
        assertThat(respuesta.getToken()).isEqualTo("jwt-firmado");
        assertThat(respuesta.getMensaje()).isNotBlank();
    }

    @Test
    @DisplayName("publica el evento de sesion iniciada y reinicia los intentos")
    void publicaEventoYReiniciaIntentos() {
        usuarioExiste();
        CredencialEntity cred = credencial("hash");
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cred));
        when(passwordEncoder.matches(PASSWORD, "hash")).thenReturn(true);
        when(jwtProvider.generarAccessToken(usuarioId)).thenReturn("jwt-firmado");

        service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD));

        verify(credencialBloqueoPolicy).reiniciarIntentosFallidos(cred);
        verify(credencialRepository).save(cred);
        verify(authEventPublisher).publicarSesionIniciada(eq(usuarioId), eq(EMAIL), any(), any());
    }

    @Test
    @DisplayName("falla si el usuario no existe")
    void fallaSiUsuarioNoExiste() {
        when(usuarioQueryService.buscarPorEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD)))
                .isInstanceOf(CredencialesInvalidasException.class);

        verify(credencialRepository, never()).findByUsuarioId(any());
    }

    @Test
    @DisplayName("falla si el usuario existe pero no tiene credencial")
    void fallaSiNoTieneCredencial() {
        usuarioExiste();
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD)))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    @DisplayName("falla con cuenta bloqueada si la credencial sigue bloqueada")
    void fallaSiCuentaBloqueada() {
        usuarioExiste();
        CredencialEntity cred = credencial("hash");
        LocalDateTime hasta = LocalDateTime.now().plusMinutes(5);
        cred.setBloqueadoHasta(hasta);
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cred));
        when(credencialBloqueoPolicy.estaBloqueada(cred)).thenReturn(true);

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD)))
                .isInstanceOf(CuentaBloqueadaException.class);

        verify(jwtProvider, never()).generarAccessToken(any());
    }

    @Test
    @DisplayName("una contrasena incorrecta cuenta un intento fallido y no devuelve token")
    void contrasenaIncorrectaCuentaIntento() {
        usuarioExiste();
        CredencialEntity cred = credencial("hash");
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cred));
        when(passwordEncoder.matches(PASSWORD, "hash")).thenReturn(false);
        when(credencialBloqueoPolicy.estaBloqueada(cred)).thenReturn(false);

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD)))
                .isInstanceOf(CredencialesInvalidasException.class);

        verify(credencialBloqueoPolicy).registrarIntentoFallido(cred);
        verify(credencialRepository).save(cred);
        verify(jwtProvider, never()).generarAccessToken(any());
    }

    @Test
    @DisplayName("un hash nulo se trata como contrasena incorrecta")
    void hashNuloEsCredencialInvalida() {
        usuarioExiste();
        CredencialEntity cred = credencial(null);
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cred));
        when(credencialBloqueoPolicy.estaBloqueada(cred)).thenReturn(false);

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD)))
                .isInstanceOf(CredencialesInvalidasException.class);

        // No debe ni siquiera preguntar por el encoder: el hash es null.
        verify(passwordEncoder, never()).matches(anyString(), any());
    }

    @Test
    @DisplayName("publica el evento de cuenta bloqueada cuando el intento fallido la bloquea")
    void publicaEventoCuentaBloqueada() {
        usuarioExiste();
        CredencialEntity cred = credencial("hash");
        when(credencialRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cred));
        when(passwordEncoder.matches(PASSWORD, "hash")).thenReturn(false);
        // Tras registrar el intento, la cuenta queda bloqueada.
        when(credencialBloqueoPolicy.estaBloqueada(cred)).thenReturn(false, true);

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO(EMAIL, PASSWORD)))
                .isInstanceOf(CredencialesInvalidasException.class);

        verify(authEventPublisher).publicarCuentaBloqueada(eq(usuarioId), eq(EMAIL), anyInt(), any());
    }
}
