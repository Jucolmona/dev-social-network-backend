package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;

public interface UserProfileRepositoryPort {

    Optional<UserProfileEntity> buscarPorUsuarioId(UUID usuarioId);

    UserProfileEntity guardar(UserProfileEntity userProfileEntity);
}