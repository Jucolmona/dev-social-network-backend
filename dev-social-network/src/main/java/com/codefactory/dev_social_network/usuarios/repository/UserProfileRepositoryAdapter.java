package com.codefactory.dev_social_network.usuarios.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileRepositoryPort;

@Component
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {

    private final UserProfileRepository userProfileRepository;

    public UserProfileRepositoryAdapter(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfileEntity> buscarPorUsuarioId(UUID usuarioId) {
        return userProfileRepository.findByUserId_Id(usuarioId);
    }

    @Override
    public UserProfileEntity guardar(UserProfileEntity userProfileEntity) {
        return userProfileRepository.save(userProfileEntity);
    }
}