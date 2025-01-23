package com.project.blog.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String SCHEMA_NAME = "bearerSchema";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(SCHEMA_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEMA_NAME, new SecurityScheme()
                                .name(SCHEMA_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")))
                .info(new Info()
                        .title("Blog Application")
                        .description("Blog Application")
                        .contact(new Contact().url("https://www.linkedin.com/in/priyanshu-baral-944967199/").name("Priyanshu Baral").email("PriyanshuBaral9562@gmail.com"))
                );
    }

}
