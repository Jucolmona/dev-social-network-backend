package com.codefactory.dev_social_network.usuarios.repository;

import java.util.UUID;
import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;
import org.springframework.data.repository.Repository;
import java.util.List;

public interface UserProfileRepository extends Repository<UserProfileEntity, UUID> {
    UserProfileEntity findById(UUID id);
    UserProfileEntity findByUserId(UUID userId);
    List<UserProfileEntity> findBySeniorityLevel(String seniorityLevel);
    UserProfileEntity save(UserProfileEntity userProfileEntity);
}
