package com.codefactory.dev_social_network.autenticacion.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtProviderTest {

    // 96 caracteres: HS256 exige una clave de al menos 32 bytes.
    private static final String SECRET = "82c5b95df6a326de633e816cfaf5ec9726ebdd6cb9417af63714c668c5d6b78efe";

    private final JwtProvider provider = new JwtProvider();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(provider, "secret", SECRET);
        ReflectionTestUtils.setField(provider, "accessTtlMinutos", 15);
    }

    @Test
    @DisplayName("genera un token cuyo subject es el id del usuario")
    void generaTokenConSubjectUsuario() {
        UUID usuarioId = UUID.randomUUID();

        String token = provider.generarAccessToken(usuarioId);

        assertThat(token).isNotBlank();
        assertThat(provider.obtenerUsuarioId(token)).isEqualTo(usuarioId);
    }

    @Test
    @DisplayName("el token recien generado es valido")
    void tokenGeneradoEsValido() {
        String token = provider.generarAccessToken(UUID.randomUUID());

        assertThat(provider.esTokenValido(token)).isTrue();
    }

    @Test
    @DisplayName("un token con otra firma no es valido")
    void tokenConOtraFirmaNoEsValido() {
        JwtProvider otro = new JwtProvider();
        ReflectionTestUtils.setField(otro, "secret",
                "clave_totalmente_distinta_que_al_menos_tiene_32_caracteres");
        ReflectionTestUtils.setField(otro, "accessTtlMinutos", 15);

        String tokenAjeno = otro.generarAccessToken(UUID.randomUUID());

        assertThat(provider.esTokenValido(tokenAjeno)).isFalse();
    }

    @Test
    @DisplayName("una cadena sin formato JWT no es valida")
    void cadenaSinFormatoNoEsValida() {
        assertThat(provider.esTokenValido("esto-no-es-un-jwt")).isFalse();
    }

    @Test
    @DisplayName("el TTL de acceso es el configurado")
    void ttlEsElConfigurado() {
        assertThat(provider.getAccessTtlMinutos()).isEqualTo(15);
    }
}
