package com.codefactory.dev_social_network.usuarios.service;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.shared.exception.EmailDuplicadoException;
import com.codefactory.dev_social_network.shared.exception.FormatoEmailInvalidoException;
import com.codefactory.dev_social_network.shared.exception.PasswordInseguraException;
import com.codefactory.dev_social_network.usuarios.entity.Credencial;
import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;
import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.CredencialRepositoryPort;
import com.codefactory.dev_social_network.usuarios.interfaces.PasswordHasher;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrarUsuarioUseCase;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileRepositoryPort;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;

@Service
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final CredencialRepositoryPort credencialRepositoryPort;
    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final PasswordHasher passwordHasher;

    public RegistrarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepositoryPort,
                                       CredencialRepositoryPort credencialRepositoryPort,
                                       UserProfileRepositoryPort userProfileRepositoryPort,
                                       PasswordHasher passwordHasher) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.credencialRepositoryPort = credencialRepositoryPort;
        this.userProfileRepositoryPort = userProfileRepositoryPort;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional
    public UUID registrar(String email, String nombre, String apellido, String contraseña) {

        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new FormatoEmailInvalidoException(email);
        }

        if (usuarioRepositoryPort.existePorEmail(email)) {
            throw new EmailDuplicadoException(email);
        }

        validarSeguridadContraseña(contraseña);

        Usuario usuarioGuardado = usuarioRepositoryPort.guardar(
                new Usuario(nombre, apellido, email));
        credencialRepositoryPort.guardar(
                new Credencial(usuarioGuardado, passwordHasher.hashear(contraseña)));

        // Perfil recién creado: vacío, excepto correo y nombre/apellidos (heredados del Usuario).
        userProfileRepositoryPort.guardar(new UserProfileEntity(usuarioGuardado));

        return usuarioGuardado.getId();
    }

    private void validarSeguridadContraseña(String contraseña) {
        if (contraseña.length() < 8) {
            throw new PasswordInseguraException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (!contraseña.chars().anyMatch(Character::isUpperCase)) {
            throw new PasswordInseguraException("La contraseña debe tener al menos 1 letra mayúscula.");
        }
        if (!contraseña.chars().anyMatch(Character::isDigit)) {
            throw new PasswordInseguraException("La contraseña debe tener al menos 1 número.");
        }
        if (contraseña.chars().allMatch(Character::isLetterOrDigit)) {
            throw new PasswordInseguraException("La contraseña debe tener al menos 1 carácter especial.");
        }
    }
}