package com.codefactory.dev_social_network.catalogoTech.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.repository.Repository;
import com.codefactory.dev_social_network.catalogoTech.entity.TecnologiesEntity;
import com.codefactory.dev_social_network.catalogoTech.entity.TecnologyCategory;

public interface TecnologiesRepository extends Repository<TecnologiesEntity, UUID> {
    Optional<TecnologiesEntity> findById(UUID id);
    List<TecnologiesEntity> findAll();
    Optional<TecnologiesEntity> findByNameTecnology(String nameTecnology);
    List<TecnologiesEntity> findByCategory(TecnologyCategory category);
    boolean existsByNameTecnologyIgnoreCase(String nameTecnology);
    TecnologiesEntity save(TecnologiesEntity tecnology);
}
