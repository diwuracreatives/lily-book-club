package com.lilybookclub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class Swagger {
    @Bean
    public OpenAPI lilyBookClubOpenAPIConfiguration() {
        return new OpenAPI()
                .info(new Info()
                .title("LilyBookClub🌸")
                .description("LilyBookClub API")
                .version("1.0"));
    }
}