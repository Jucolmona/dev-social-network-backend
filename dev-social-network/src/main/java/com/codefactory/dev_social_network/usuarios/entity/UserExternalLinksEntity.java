package com.codefactory.dev_social_network.usuarios.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "user_external_links")
public class UserExternalLinksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column (name = "link_name", unique = true, nullable = false)
    private String linkName;

    @Column (name = "link_url", unique = true, nullable = false)
    private String linkUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "userProfile_id", referencedColumnName = "id", nullable = false)
    private UserProfileEntity userProfile;

    protected UserExternalLinksEntity() {}

    public UUID getId() {
        return id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public UserProfileEntity getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileEntity userProfile) {
        this.userProfile = userProfile;
    }

}
