package com.codefactory.dev_social_network.discusiones.service;

import com.codefactory.dev_social_network.discusiones.dto.CreateDiscussionRequest;
import com.codefactory.dev_social_network.discusiones.dto.DiscussionResponse;
import com.codefactory.dev_social_network.discusiones.entity.Discussion;
import com.codefactory.dev_social_network.discusiones.repository.DiscussionRepository;
import com.codefactory.dev_social_network.proyectos.entity.Technology;
import com.codefactory.dev_social_network.proyectos.repository.TechnologyRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final TechnologyRepository technologyRepository;

    public DiscussionService(
            DiscussionRepository discussionRepository,
            TechnologyRepository technologyRepository
    ) {
        this.discussionRepository = discussionRepository;
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    public DiscussionResponse createDiscussion(UUID userId, CreateDiscussionRequest request) {

        Technology technology = technologyRepository.findById(request.getTechnologyId())
                .orElseThrow(() -> new BusinessException(
                        "INVALID_TECHNOLOGY",
                        "Technology does not exist",
                        HttpStatus.BAD_REQUEST
                ));

        Discussion discussion = new Discussion(
                userId,
                technology.getId(),
                request.getTitle(),
                request.getContent()
        );

        Discussion savedDiscussion = discussionRepository.save(discussion);

        return mapToResponse(savedDiscussion, technology);
    }

    private DiscussionResponse mapToResponse(Discussion discussion, Technology technology) {

        DiscussionResponse response = new DiscussionResponse();

        response.setId(discussion.getId());
        response.setUserId(discussion.getUserId());
        response.setTechnology(technology.getName());
        response.setTitle(discussion.getTitle());
        response.setContent(discussion.getContent());
        response.setStatus(discussion.getStatus());
        response.setCreatedAt(discussion.getCreatedAt());

        return response;
    }
}