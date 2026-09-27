package com.codefactory.dev_social_network.catalogoTech.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codefactory.dev_social_network.catalogoTech.entity.TecnologiesEntity;
import com.codefactory.dev_social_network.catalogoTech.entity.TecnologyCategory;
import com.codefactory.dev_social_network.catalogoTech.repository.TecnologiesRepository;

@ExtendWith(MockitoExtension.class)
class CatalogoTecnologiaServiceImplTest {

    @Mock
    private TecnologiesRepository tecnologiasRepository;

    @InjectMocks
    private CatalogoTecnologiaServiceImpl service;

    private TecnologiesEntity tecnologia(String nombre) {
        TecnologiesEntity t = new TecnologiesEntity(nombre, TecnologyCategory.LANGUAGE);
        t.setNameTecnology(nombre);
        return t;
    }

    // ---------------------------------------------- isTecnologyAvailable

    @Test
    @DisplayName("una lista de tecnologias nula se considera disponible")
    void listaNulaEsDisponible() {
        assertThat(service.isTecnologyAvailable(null)).isTrue();

        verify(tecnologiasRepository, never()).existsByNameTecnologyIgnoreCase(anyString());
    }

    @Test
    @DisplayName("una lista vacia se considera disponible")
    void listaVaciaEsDisponible() {
        assertThat(service.isTecnologyAvailable(List.of())).isTrue();
    }

    @Test
    @DisplayName("todas las tecnologias presentes la marcan como disponible")
    void todasLasTecnologiasExisten() {
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Java")).thenReturn(true);
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Spring")).thenReturn(true);

        assertThat(service.isTecnologyAvailable(List.of("Java", "Spring"))).isTrue();
    }

    @Test
    @DisplayName("una sola tecnologia ausente hace que la lista no sea valida")
    void unaTecnologiaAusenteInvalida() {
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Java")).thenReturn(true);
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Klingon")).thenReturn(false);

        assertThat(service.isTecnologyAvailable(List.of("Java", "Klingon"))).isFalse();
    }

    @Test
    @DisplayName("recorta el nombre antes de buscar, ignorando espacios sobrantes")
    void normalizaNombreTecnologia() {
        // trim() no cambia las mayusculas: por eso se busca "jAvA".
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("jAvA")).thenReturn(true);

        assertThat(service.isTecnologyAvailable(List.of("  jAvA  "))).isTrue();
    }

    @Test
    @DisplayName("un nombre de tecnologia nulo consulta el repositorio con cadena vacia")
    void nombreNuloSeBuscaComoVacio() {
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("")).thenReturn(false);

        assertThat(service.isTecnologyAvailable(java.util.Arrays.asList((String) null))).isFalse();
    }

    // ---------------------------------------------- obtenerNombresDisponibles

    @Test
    @DisplayName("devuelve los nombres de las tecnologias del catalogo")
    void devuelveNombresDisponibles() {
        when(tecnologiasRepository.findAll()).thenReturn(List.of(tecnologia("Java"), tecnologia("Spring")));

        assertThat(service.obtenerNombresDisponibles()).containsExactly("Java", "Spring");
    }

    // ------------------------------------------------------- existe

    @Test
    @DisplayName("existe devuelve true cuando la tecnologia esta en el catalogo")
    void existeEnCatalogo() {
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Java")).thenReturn(true);

        assertThat(service.existe("Java")).isTrue();
    }

    @Test
    @DisplayName("existe devuelve false cuando la tecnologia no esta en el catalogo")
    void noExisteEnCatalogo() {
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Klingon")).thenReturn(false);

        assertThat(service.existe("Klingon")).isFalse();
    }

    @Test
    @DisplayName("existe devuelve false para un nombre nulo o vacio sin consultar el repositorio")
    void existeConNombreVacio() {
        assertThat(service.existe(null)).isFalse();
        assertThat(service.existe("   ")).isFalse();

        verify(tecnologiasRepository, never()).existsByNameTecnologyIgnoreCase(anyString());
    }

    @Test
    @DisplayName("existe recorta el nombre antes de consultar")
    void existeRecortaElNombre() {
        when(tecnologiasRepository.existsByNameTecnologyIgnoreCase("Java")).thenReturn(true);

        assertThat(service.existe("  Java  ")).isTrue();
    }
}
