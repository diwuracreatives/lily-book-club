package com.lilybookclub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;


public class SwaggerConfiguration {
    @Bean
    public OpenAPI lilyBookClubOpenAPIConfiguration() {
        return new OpenAPI()
                .info(new Info()
                .title("LilyBookClub🌸")
                .description("LilyBookClub API")
                .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
                 .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                 .bearerFormat("JWT")));

    }
}