package com.codefactory.dev_social_network.usuarios.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "user_habilities")
public class UserHabilitiesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name_hability", nullable = false)
    private String nameHability;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id", nullable = false)
    private UserProfileEntity userProfile;

    protected UserHabilitiesEntity() {}

    public UserHabilitiesEntity(String nameHability, UserProfileEntity userProfile) {
        this.nameHability = nameHability;
        this.userProfile = userProfile;
    }

    public UUID getId() {
        return id;
    }

    public String getNameHability() {
        return nameHability;
    }

    public void setNameHability(String nameHability) {
        this.nameHability = nameHability;
    }

    public UserProfileEntity getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileEntity userProfile) {
        this.userProfile = userProfile;
    }

}
