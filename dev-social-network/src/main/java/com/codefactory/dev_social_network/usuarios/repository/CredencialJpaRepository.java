package com.codefactory.dev_social_network.usuarios.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codefactory.dev_social_network.usuarios.entity.Credencial;

public interface CredencialJpaRepository extends JpaRepository<Credencial, UUID> {

    Optional<Credencial> findByUsuarioId(UUID usuarioId);
}