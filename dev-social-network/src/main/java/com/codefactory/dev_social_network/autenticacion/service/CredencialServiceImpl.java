package com.codefactory.dev_social_network.autenticacion.service;

import com.codefactory.dev_social_network.autenticacion.entity.CredencialEntity;
import com.codefactory.dev_social_network.autenticacion.interfaces.CredencialService;
import com.codefactory.dev_social_network.autenticacion.repository.CredencialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredencialServiceImpl implements CredencialService {

    private final CredencialRepository credencialRepository;

    @Override
    @Transactional
    public void crearCredencialLocal(UUID usuarioId, String passwordHash) {
        CredencialEntity credencial = CredencialEntity.local(usuarioId, passwordHash);
        credencialRepository.save(credencial);
    }
}