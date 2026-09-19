package com.codefactory.dev_social_network.autenticacion.repository;

import com.codefactory.dev_social_network.autenticacion.entity.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    Optional<Credencial> findByCorreoElectronico(String correoElectronico);
}
