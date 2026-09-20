package com.codefactory.dev_social_network.usuarios.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return jpaRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email);
    }
}