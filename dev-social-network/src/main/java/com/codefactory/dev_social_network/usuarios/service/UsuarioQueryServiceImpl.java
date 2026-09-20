package com.codefactory.dev_social_network.usuarios.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codefactory.dev_social_network.usuarios.dto.UsuarioDTO;
import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.CredencialRepositoryPort;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioQueryService;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;

@Service
public class UsuarioQueryServiceImpl implements UsuarioQueryService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final CredencialRepositoryPort credencialRepositoryPort;

    public UsuarioQueryServiceImpl(UsuarioRepositoryPort usuarioRepositoryPort,
                                   CredencialRepositoryPort credencialRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.credencialRepositoryPort = credencialRepositoryPort;
    }

    @Override
    public Optional<UsuarioDTO> buscarPorEmail(String email) {
        return usuarioRepositoryPort.buscarPorEmail(email).map(this::aDto);
    }

    private UsuarioDTO aDto(Usuario usuario) {
        String hash = credencialRepositoryPort.buscarPorUsuarioId(usuario.getId())
                .map(c -> c.getPasswordHash())
                .orElse(null);
        return new UsuarioDTO(usuario.getId(), usuario.getEmail(), hash);
    }
}