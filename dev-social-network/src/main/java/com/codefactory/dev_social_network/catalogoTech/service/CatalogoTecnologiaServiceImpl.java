package com.codefactory.dev_social_network.catalogoTech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codefactory.dev_social_network.catalogoTech.interfaces.CatalogoTecnologiaApi;
import com.codefactory.dev_social_network.catalogoTech.repository.TecnologiesRepository;

@Service
public class CatalogoTecnologiaServiceImpl implements CatalogoTecnologiaApi {

    private final TecnologiesRepository tecnologiasRepository;

    public CatalogoTecnologiaServiceImpl(TecnologiesRepository tecnologiasRepository) {
        this.tecnologiasRepository = tecnologiasRepository;
    }

    @Override
    public boolean isTecnologyAvailable(List<String> nameTecnology) {
        if (nameTecnology == null) {
            return true;
        }
        return nameTecnology.stream()
                .allMatch(nombre -> tecnologiasRepository
                        .existsByNameTecnologyIgnoreCase(nombre == null ? "" : nombre.trim()));
    }

    @Override
    public List<String> obtenerNombresDisponibles() {
        return tecnologiasRepository.findAll()
                .stream()
                .map(tecnologia -> tecnologia.getNameTecnology())
                .toList();
    }

    public boolean existe(String nameTecnology) {
        if (nameTecnology == null || nameTecnology.isBlank()) {
            return false;
        }
        return tecnologiasRepository.existsByNameTecnologyIgnoreCase(nameTecnology.trim());
    }
}