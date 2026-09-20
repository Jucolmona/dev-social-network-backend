package com.codefactory.dev_social_network.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI codeFactoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CodeF@tory API")
                        .version("1.0.0")
                        .description(
                                "REST API for the CodeF@tory social network for developers"
                        ));
    }
}