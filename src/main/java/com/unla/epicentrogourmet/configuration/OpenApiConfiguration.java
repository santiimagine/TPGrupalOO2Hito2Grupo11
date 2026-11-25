package com.unla.epicentrogourmet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {

    @Bean
    OpenAPI epicentroGourmetOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API Epicentro Gourmet")
                .description("API REST del sistema de gestion Epicentro Gourmet. "
                        + "Hito 3 del TP de Orientacion a Objetos II, Grupo 11.")
                .version("v1"));
    }
}
