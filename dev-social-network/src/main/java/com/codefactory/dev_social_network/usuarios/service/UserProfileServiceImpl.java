package com.codefactory.dev_social_network.usuarios.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.catalogoTech.service.CatalogoTecnologiaServiceImpl;
import com.codefactory.dev_social_network.shared.exception.AniosExperienciaInvalidosException;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.shared.exception.HabilidadNoValidaException;
import com.codefactory.dev_social_network.shared.exception.NivelExperienciaInvalidoException;
import com.codefactory.dev_social_network.usuarios.dto.ExternalLinkDTO;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileEditionRequestDTO;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileResponseDTO;
import com.codefactory.dev_social_network.usuarios.entity.ExperienceLevel;
import com.codefactory.dev_social_network.usuarios.entity.UserExternalLinksEntity;
import com.codefactory.dev_social_network.usuarios.entity.UserHabilitiesEntity;
import com.codefactory.dev_social_network.usuarios.entity.UserProfileEntity;
import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileRepositoryPort;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileService;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioRepositoryPort;
import com.codefactory.dev_social_network.usuarios.repository.UserExternalLinksRepository;
import com.codefactory.dev_social_network.usuarios.repository.UserHabilitiesRepository;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final UserExternalLinksRepository externalLinksRepository;
    private final UserHabilitiesRepository habilitiesRepository;
    private final CatalogoTecnologiaServiceImpl catalogoTecnologiaService;
    private final EnlaceExternoValidator enlaceExternoValidator;

    public UserProfileServiceImpl(
            UserProfileRepositoryPort userProfileRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort,
            UserExternalLinksRepository externalLinksRepository,
            UserHabilitiesRepository habilitiesRepository,
            CatalogoTecnologiaServiceImpl catalogoTecnologiaService,
            EnlaceExternoValidator enlaceExternoValidator) {
        this.userProfileRepositoryPort = userProfileRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.externalLinksRepository = externalLinksRepository;
        this.habilitiesRepository = habilitiesRepository;
        this.catalogoTecnologiaService = catalogoTecnologiaService;
        this.enlaceExternoValidator = enlaceExternoValidator;
    }

    @Override
    @Transactional
    public UserProfileResponseDTO updateUserProfile(UUID userId, UserProfileEditionRequestDTO request) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(userId)
                .orElseThrow(() -> usuarioNoEncontrado(userId));

        UserProfileEntity profile = userProfileRepositoryPort.buscarPorUsuarioId(userId)
                .orElseGet(() -> new UserProfileEntity(usuario));

        validateYearsOfExperience(request.getYearsOfExperience());
        validateLevel(request.getLevel());
        validateSkills(request.getSkills());
        enlaceExternoValidator.validarGithub(request.getGithubLink());
        enlaceExternoValidator.validarLinkedin(request.getLinkedinLink());
        enlaceExternoValidator.validarPortafolio(request.getPortfolioLink());

        profile.setYearsOfExperience(request.getYearsOfExperience());
        profile.setSeniorityLevel(parseLevel(request.getLevel()));
        profile.setPorfolioLink(request.getPortfolioLink());

        userProfileRepositoryPort.guardar(profile);

        syncExternalLinks(profile, request);
        syncHabilities(profile, request);

        return buildResponse(profile, usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDTO getUserProfile(UUID userId) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(userId)
                .orElseThrow(() -> usuarioNoEncontrado(userId));

        UserProfileEntity profile = userProfileRepositoryPort.buscarPorUsuarioId(userId)
                .orElseGet(() -> new UserProfileEntity(usuario));

        return buildResponse(profile, usuario);
    }

    private BusinessException usuarioNoEncontrado(UUID userId) {
        return new BusinessException(
                "USUARIO_NO_ENCONTRADO",
                "No se encontró un usuario con id " + userId,
                HttpStatus.NOT_FOUND);
    }

    private void validateYearsOfExperience(Integer years) {
        if (years == null) {
            return;
        }
        if (years < 0 || years > 20) {
            throw new AniosExperienciaInvalidosException(years);
        }
    }

    private void validateLevel(String level) {
        if (level == null || level.isBlank()) {
            return;
        }
        parseLevel(level);
    }

    private ExperienceLevel parseLevel(String level) {
        if (level == null || level.isBlank()) {
            return null;
        }
        try {
            return ExperienceLevel.valueOf(level.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NivelExperienciaInvalidoException(level);
        }
    }

    private void validateSkills(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return;
        }
        for (String skill : skills) {
            if (skill == null || !catalogoTecnologiaService.existe(skill)) {
                throw new HabilidadNoValidaException(skill);
            }
        }
    }

    private void syncExternalLinks(UserProfileEntity profile, UserProfileEditionRequestDTO request) {
        externalLinksRepository.deleteByUserProfile_Id(profile.getId());

        if (notBlank(request.getGithubLink())) {
            externalLinksRepository.save(
                    new UserExternalLinksEntity("github", request.getGithubLink().trim(), profile));
        }
        if (notBlank(request.getLinkedinLink())) {
            externalLinksRepository.save(
                    new UserExternalLinksEntity("linkedin", request.getLinkedinLink().trim(), profile));
        }
        if (notBlank(request.getPortfolioLink())) {
            externalLinksRepository.save(
                    new UserExternalLinksEntity("portafolio", request.getPortfolioLink().trim(), profile));
        }
    }

    private void syncHabilities(UserProfileEntity profile, UserProfileEditionRequestDTO request) {
        habilitiesRepository.deleteByUserProfile_Id(profile.getId());

        if (request.getSkills() != null) {
            for (String skill : request.getSkills()) {
                if (skill != null && !skill.isBlank()) {
                    habilitiesRepository.save(new UserHabilitiesEntity(skill.trim(), profile));
                }
            }
        }
    }

    private UserProfileResponseDTO buildResponse(UserProfileEntity profile, Usuario usuario) {
        List<ExternalLinkDTO> links = externalLinksRepository.findByUserProfile_Id(profile.getId())
                .stream()
                .map(link -> new ExternalLinkDTO(link.getLinkName(), link.getLinkUrl()))
                .toList();

        List<String> skills = habilitiesRepository.findByUserProfile_Id(profile.getId())
                .stream()
                .map(UserHabilitiesEntity::getNameHability)
                .toList();

        return new UserProfileResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                links,
                skills,
                profile.getYearsOfExperience(),
                profile.getSeniorityLevel());
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}