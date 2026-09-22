package com.codefactory.dev_social_network.usuarios.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;

@Entity 
@Table(name = "user_habilities")
public class UserHabilitiesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name_hanility", unique = true, nullable = false)
    private String nameHability;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "userProfile_id", referencedColumnName = "id", nullable = false)
    private UserProfileEntity userProfile;

    // aditio tecnoligies

    protected UserHabilitiesEntity() {}

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
