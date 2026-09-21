package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.UUID;

public interface RegistrarUsuarioUseCase {

    UUID registrar(String email, String contraseña);

}