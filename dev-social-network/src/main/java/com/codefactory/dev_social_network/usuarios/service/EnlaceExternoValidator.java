package com.codefactory.dev_social_network.usuarios.service;

import java.net.URI;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.codefactory.dev_social_network.shared.exception.EnlaceInvalidoException;

@Component
public class EnlaceExternoValidator {

    private static final Pattern GITHUB_PATTERN =
            Pattern.compile("^http://github\\.com/[a-zA-Z0-9-]+/?$");

    private static final Pattern LINKEDIN_PATTERN =
            Pattern.compile("^https://www\\.linkedin\\.com/in/[a-zA-Z0-9-]+/$");

    public void validarGithub(String githubLink) {
        if (githubLink == null || githubLink.isBlank()) {
            return;
        }
        if (!GITHUB_PATTERN.matcher(githubLink.trim()).matches()) {
            throw new EnlaceInvalidoException(
                    "El enlace de GitHub no es válido. Debe cumplir el patrón: http://github.com/[nombre de usuario]");
        }
    }

    public void validarLinkedin(String linkedinLink) {
        if (linkedinLink == null || linkedinLink.isBlank()) {
            return;
        }
        if (!LINKEDIN_PATTERN.matcher(linkedinLink.trim()).matches()) {
            throw new EnlaceInvalidoException(
                    "El enlace de LinkedIn no es válido. Debe cumplir el patrón: https://www.linkedin.com/in/[nombre de usuario]/");
        }
    }

    public void validarPortafolio(String portfolioLink) {
        if (portfolioLink == null || portfolioLink.isBlank()) {
            return;
        }
        try {
            URI uri = URI.create(portfolioLink.trim());
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw new EnlaceInvalidoException(
                    "El enlace del portafolio no es válido. Debe ser una URL válida (por ejemplo: https://midominio.com)");
        }
    }
}