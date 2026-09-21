package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.Optional;

import com.codefactory.dev_social_network.usuarios.dto.UsuarioDTO;

public interface UsuarioQueryService {

    Optional<UsuarioDTO> buscarPorEmail(String email);

}