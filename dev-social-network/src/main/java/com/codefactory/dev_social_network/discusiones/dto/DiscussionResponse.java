package com.codefactory.dev_social_network.discusiones.dto;

import com.codefactory.dev_social_network.discusiones.entity.DiscussionStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DiscussionResponse {

    private Long id;
    private UUID userId;
    private String technology;
    private String title;
    private String content;
    private DiscussionStatus status;
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(DiscussionStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
