package com.codefactory.dev_social_network.usuarios.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.usuarios.entity.Credencial;
import com.codefactory.dev_social_network.usuarios.interfaces.CredencialRepositoryPort;

@Component
public class CredencialRepositoryAdapter implements CredencialRepositoryPort {

    private final CredencialJpaRepository jpaRepository;

    public CredencialRepositoryAdapter(CredencialJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Credencial guardar(Credencial credencial) {
        return jpaRepository.save(credencial);
    }

    @Override
    public Optional<Credencial> buscarPorUsuarioId(UUID usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId);
    }
}