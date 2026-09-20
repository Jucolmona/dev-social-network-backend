package com.codefactory.dev_social_network.usuarios.service;

import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrarUsuarioUseCase;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.EmailDuplicadoException;
import com.codefactory.dev_social_network.shared.exception.FormatoEmailInvalidoException;
import com.codefactory.dev_social_network.shared.exception.PasswordInseguraException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RegistrarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UUID registrar(String email, String contraseña) {

        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new FormatoEmailInvalidoException(email);
        }

        if (usuarioRepositoryPort.existePorEmail(email)) {
            throw new EmailDuplicadoException(email);
        }

        validarSeguridadContraseña(contraseña);

        String contraseñaHash = passwordEncoder.encode(contraseña);
        Usuario nuevoUsuario = new Usuario(email, contraseñaHash);
        Usuario usuarioGuardado = usuarioRepositoryPort.guardar(nuevoUsuario);

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