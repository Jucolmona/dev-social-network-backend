package com.codefactory.dev_social_network.usuarios.dto;

import java.util.List;
import java.util.UUID;

import com.codefactory.dev_social_network.usuarios.entity.ExperienceLevel;

public record UserProfileResponseDTO(
        UUID userId,
        String nombre,
        String apellidos,
        String email,
        List<ExternalLinkDTO> externalLinks,
        List<String> skills,
        Integer yearsOfExperience,
        ExperienceLevel level
) {
}