package com.codefactory.dev_social_network.usuarios.repository;

import com.codefactory.dev_social_network.usuarios.entity.UserHabilitiesEntity;
import org.springframework.data.repository.Repository;
import java.util.UUID;

public interface UserHabilitiesRepository extends Repository<UserHabilitiesEntity, UUID> {
    UserHabilitiesEntity save(UserHabilitiesEntity userHabilitiesEntity);
}
