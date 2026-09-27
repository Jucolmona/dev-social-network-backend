package com.codefactory.dev_social_network.usuarios.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codefactory.dev_social_network.usuarios.entity.Usuario;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @Mock
    private UsuarioJpaRepository jpaRepository;

    @InjectMocks
    private UsuarioRepositoryAdapter adapter;

    @Test
    @DisplayName("existePorEmail delega en el repositorio JPA")
    void existePorEmail() {
        when(jpaRepository.existsByEmail("ana@correo.com")).thenReturn(true);

        assertThat(adapter.existePorEmail("ana@correo.com")).isTrue();
        verify(jpaRepository).existsByEmail("ana@correo.com");
    }

    @Test
    @DisplayName("existePorEmail devuelve false cuando el email no esta registrado")
    void existePorEmailDevuelveFalse() {
        when(jpaRepository.existsByEmail("nuevo@correo.com")).thenReturn(false);

        assertThat(adapter.existePorEmail("nuevo@correo.com")).isFalse();
    }

    @Test
    @DisplayName("guardar delega en el repositorio JPA")
    void guardar() {
        Usuario usuario = new Usuario("Ana", "Ruiz", "ana@correo.com");
        when(jpaRepository.save(usuario)).thenReturn(usuario);

        assertThat(adapter.guardar(usuario)).isSameAs(usuario);
        verify(jpaRepository).save(usuario);
    }

    @Test
    @DisplayName("buscarPorEmail devuelve el usuario encontrado")
    void buscarPorEmail() {
        Usuario usuario = new Usuario("Ana", "Ruiz", "ana@correo.com");
        when(jpaRepository.findByEmail("ana@correo.com")).thenReturn(Optional.of(usuario));

        assertThat(adapter.buscarPorEmail("ana@correo.com")).contains(usuario);
    }

    @Test
    @DisplayName("buscarPorEmail devuelve vacio si no existe")
    void buscarPorEmailNoEncontrado() {
        when(jpaRepository.findByEmail("nadie@correo.com")).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorEmail("nadie@correo.com")).isEmpty();
    }

    @Test
    @DisplayName("buscarPorId devuelve el usuario encontrado")
    void buscarPorId() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario("Ana", "Ruiz", "ana@correo.com");
        when(jpaRepository.findById(id)).thenReturn(Optional.of(usuario));

        assertThat(adapter.buscarPorId(id)).contains(usuario);
    }

    @Test
    @DisplayName("buscarPorId devuelve vacio si no existe")
    void buscarPorIdNoEncontrado() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorId(id)).isEmpty();
    }
}
