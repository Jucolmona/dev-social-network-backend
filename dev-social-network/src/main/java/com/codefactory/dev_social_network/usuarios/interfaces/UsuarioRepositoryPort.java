package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.codefactory.dev_social_network.usuarios.entity.Usuario;

public interface UsuarioRepositoryPort {

    boolean existePorEmail(String email);

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorId(UUID id);
}