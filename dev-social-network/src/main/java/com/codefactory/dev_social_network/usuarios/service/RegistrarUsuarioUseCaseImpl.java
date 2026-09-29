package com.codefactory.dev_social_network.usuarios.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.autenticacion.interfaces.CredencialService;
import com.codefactory.dev_social_network.shared.exception.EmailDuplicadoException;
import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;
import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.PasswordHasher;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrarUsuarioUseCase;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileRepositoryPort;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;

@Service
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final CredencialService credencialService;
    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final PasswordHasher passwordHasher;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public RegistrarUsuarioUseCaseImpl(
            UsuarioRepositoryPort usuarioRepositoryPort,
            CredencialService credencialService,
            UserProfileRepositoryPort userProfileRepositoryPort,
            PasswordHasher passwordHasher,
            EmailValidator emailValidator,
            PasswordValidator passwordValidator) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.credencialService = credencialService;
        this.userProfileRepositoryPort = userProfileRepositoryPort;
        this.passwordHasher = passwordHasher;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    @Transactional
    public UUID registrar(String email, String nombre, String apellido, String contrasena) {

        emailValidator.validar(email);
        passwordValidator.validar(contrasena);

        if (usuarioRepositoryPort.existePorEmail(email)) {
            throw new EmailDuplicadoException(email);
        }

        Usuario usuarioGuardado = usuarioRepositoryPort.guardar(new Usuario(nombre, apellido, email));

        credencialService.crearCredencialLocal(
                usuarioGuardado.getId(), passwordHasher.hashear(contrasena));

        userProfileRepositoryPort.guardar(new UserProfileEntity(usuarioGuardado));

        return usuarioGuardado.getId();
    }
}