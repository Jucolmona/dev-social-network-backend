package com.codefactory.dev_social_network.autenticacion.repository;

import com.codefactory.dev_social_network.autenticacion.entity.AccountLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountLockRepository extends JpaRepository<AccountLock, UUID> {
    Optional<AccountLock> findByUsuarioId(UUID usuarioId);
}
