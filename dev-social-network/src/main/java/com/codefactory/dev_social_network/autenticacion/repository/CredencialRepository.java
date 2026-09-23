package com.codefactory.dev_social_network.autenticacion.repository;

import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredencialRepository extends JpaRepository<CredencialEntity, UUID> {
    Optional<CredencialEntity> findByUsuarioId(UUID usuarioId);
}