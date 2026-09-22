package com.codefactory.dev_social_network.usuarios.repository;

import com.codefactory.dev_social_network.usuarios.entity.UserExternalLinksEntity;
import org.springframework.data.repository.Repository;
import java.util.UUID;

public interface UserExternalLinks extends Repository<UserExternalLinksEntity, UUID> {
    UserExternalLinksEntity save(UserExternalLinksEntity userExternalLinksEntity);
}
