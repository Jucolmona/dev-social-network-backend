package com.codefactory.dev_social_network.catalogoTech.interfaces;

import java.util.List;

public interface CatalogoTecnologiaApi {
    boolean isTecnologyAvailable(List<String> nameTecnology);

    List<String> obtenerNombresDisponibles();
}
