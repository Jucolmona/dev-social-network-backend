package com.codefactory.dev_social_network.catalogoTech.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codefactory.dev_social_network.catalogoTech.entity.TecnologiesEntity;
import com.codefactory.dev_social_network.catalogoTech.entity.TecnologyCategory;
import com.codefactory.dev_social_network.catalogoTech.repository.TecnologiesRepository;

@Configuration
public class CatalogoTecnologiaDataInitializer {

    @Bean
    CommandLineRunner cargarCatalogoTecnologia(TecnologiesRepository repository) {
        return args -> seed(repository);
    }

    private void seed(TecnologiesRepository repository) {
        if (repository.findAll().size() > 0) {
            return;
        }

        List<TecnologiesEntity> seed = List.of(
                // Roles
                new TecnologiesEntity("Frontend Developer", TecnologyCategory.ROLE),
                new TecnologiesEntity("Backend Developer", TecnologyCategory.ROLE),
                new TecnologiesEntity("Full Stack Developer", TecnologyCategory.ROLE),
                new TecnologiesEntity("DevOps Engineer", TecnologyCategory.ROLE),
                new TecnologiesEntity("Data Engineer", TecnologyCategory.ROLE),
                new TecnologiesEntity("QA Engineer", TecnologyCategory.ROLE),
                new TecnologiesEntity("Mobile Developer", TecnologyCategory.ROLE),
                new TecnologiesEntity("Security Engineer", TecnologyCategory.ROLE),
                // Lenguajes
                new TecnologiesEntity("Java", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("JavaScript", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("TypeScript", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("Python", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("C#", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("Go", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("Kotlin", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("Rust", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("PHP", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("Ruby", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("SQL", TecnologyCategory.LANGUAGE),
                new TecnologiesEntity("Swift", TecnologyCategory.LANGUAGE),
                // Frameworks
                new TecnologiesEntity("Spring Boot", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("React", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Angular", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Vue", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Node.js", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Django", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Flask", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity(".NET", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Laravel", TecnologyCategory.FRAMEWORK),
                new TecnologiesEntity("Flutter", TecnologyCategory.FRAMEWORK)
        );

        for (TecnologiesEntity tecnologia : seed) {
            repository.save(tecnologia);
        }
    }
}