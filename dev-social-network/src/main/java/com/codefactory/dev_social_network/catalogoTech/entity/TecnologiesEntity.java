package com.codefactory.dev_social_network.catalogoTech.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "tecnologies")
public class TecnologiesEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name = "name_tecnology", unique = true, nullable = false)
    private String nameTecnology;

    @Column (name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private TecnologyCategory category;

    protected TecnologiesEntity() {}

    public TecnologiesEntity(String nameTecnology, TecnologyCategory category) {
        this.nameTecnology = nameTecnology;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public String getNameTecnology() {
        return nameTecnology;
    }

    public void setNameTecnology(String nameTecnology) {
        this.nameTecnology = nameTecnology;
    }

    public TecnologyCategory getCategory() {
        return category;
    }

    public void setCategory(TecnologyCategory category) {
        this.category = category;
    }

}
