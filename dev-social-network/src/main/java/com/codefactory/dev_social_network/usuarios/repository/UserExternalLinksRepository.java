package com.codefactory.dev_social_network.usuarios.repository;

import com.codefactory.dev_social_network.usuarios.entity.UserExternalLinksEntity;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface UserExternalLinksRepository extends Repository<UserExternalLinksEntity, UUID> {
    UserExternalLinksEntity save(UserExternalLinksEntity userExternalLinksEntity);
    List<UserExternalLinksEntity> findByUserProfile_Id(UUID userProfileId);
    void deleteByUserProfile_Id(UUID userProfileId);
}