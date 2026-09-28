package com.codefactory.dev_social_network.usuarios.repository;

import java.util.UUID;

import com.codefactory.dev_social_network.usuarios.entity.ExperienceLevel;
import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;
import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends Repository<UserProfileEntity, UUID> {
    Optional<UserProfileEntity> findById(UUID id);
    Optional<UserProfileEntity> findByUserId_Id(UUID userId);
    List<UserProfileEntity> findBySeniorityLevel(ExperienceLevel seniorityLevel);
    UserProfileEntity save(UserProfileEntity userProfileEntity);
}
