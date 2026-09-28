package com.codefactory.dev_social_network.usuarios.repository;

import com.codefactory.dev_social_network.usuarios.entity.UserHabilitiesEntity;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface UserHabilitiesRepository extends Repository<UserHabilitiesEntity, UUID> {
    UserHabilitiesEntity save(UserHabilitiesEntity userHabilitiesEntity);
    List<UserHabilitiesEntity> findByUserProfile_Id(UUID userProfileId);
    void deleteByUserProfile_Id(UUID userProfileId);
}
