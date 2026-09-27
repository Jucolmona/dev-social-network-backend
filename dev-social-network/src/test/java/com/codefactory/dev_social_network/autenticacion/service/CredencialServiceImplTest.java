package com.codefactory.dev_social_network.autenticacion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import com.codefactory.dev_social_network.autenticacion.repository.CredencialRepository;

@ExtendWith(MockitoExtension.class)
class CredencialServiceImplTest {

    @Mock
    private CredencialRepository credencialRepository;

    @InjectMocks
    private CredencialServiceImpl service;

    @Test
    @DisplayName("guarda la credencial con tipo LOCAL y el usuario recibido")
    void guardaCredencialLocal() {
        UUID usuarioId = UUID.randomUUID();

        service.crearCredencialLocal(usuarioId, "hash123");

        ArgumentCaptor<CredencialEntity> captor = ArgumentCaptor.forClass(CredencialEntity.class);
        verify(credencialRepository, times(1)).save(captor.capture());

        CredencialEntity guardada = captor.getValue();
        assertThat(guardada.getUsuarioId()).isEqualTo(usuarioId);
        assertThat(guardada.getTipo()).isEqualTo("LOCAL");
        assertThat(guardada.getPasswordHash()).isEqualTo("hash123");
        assertThat(guardada.getIntentosFallidos()).isZero();
    }

    @Test
    @DisplayName("la credencial creada no tiene proveedor externo ni bloqueo")
    void credencialSinProveedorNiBloqueo() {
        service.crearCredencialLocal(UUID.randomUUID(), "hash123");

        ArgumentCaptor<CredencialEntity> captor = ArgumentCaptor.forClass(CredencialEntity.class);
        verify(credencialRepository).save(captor.capture());

        assertThat(captor.getValue().getProveedorIdExterno()).isNull();
        assertThat(captor.getValue().getBloqueadoHasta()).isNull();
    }

    @Test
    @DisplayName("delega el guardado en el repositorio")
    void delegaElGuardado() {
        service.crearCredencialLocal(UUID.randomUUID(), "hash123");

        verify(credencialRepository).save(any(CredencialEntity.class));
    }
}
