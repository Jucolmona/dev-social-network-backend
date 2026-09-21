package com.codefactory.dev_social_network.usuarios.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codefactory.dev_social_network.usuarios.dto.UsuarioDTO;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioQueryService;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;

@Service
public class UsuarioQueryServiceImpl implements UsuarioQueryService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public UsuarioQueryServiceImpl(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Optional<UsuarioDTO> buscarPorEmail(String email) {
        return usuarioRepositoryPort.buscarPorEmail(email)
                .map(usuario -> new UsuarioDTO(usuario.getId(), usuario.getEmail()));
    }
}