package com.codefactory.dev_social_network.usuarios.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codefactory.dev_social_network.usuarios.dto.CredencialAuthDTO;
import com.codefactory.dev_social_network.usuarios.dto.UsuarioDTO;
import com.codefactory.dev_social_network.usuarios.entity.Credencial;
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
        return usuarioRepositoryPort.buscarPorEmail(email)
                .map(this::aUsuarioDto);
    }

    @Override
    public Optional<CredencialAuthDTO> buscarCredencialPorEmail(String email) {
        return usuarioRepositoryPort.buscarPorEmail(email)
                .flatMap(this::aCredencialAuthDto);
    }

    private UsuarioDTO aUsuarioDto(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getEmail());
    }

    private Optional<CredencialAuthDTO> aCredencialAuthDto(Usuario usuario) {
        return credencialRepositoryPort.buscarPorUsuarioId(usuario.getId())
                .map(credencial -> new CredencialAuthDTO(
                        usuario.getId(),
                        usuario.getEmail(),
                        credencial.getPasswordHash()));
    }
}