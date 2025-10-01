package com.packt.car_database_be;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carDatabaseBeOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("Car REST API")
                        .description("Car Database")
                        .version("1.0"));
    }
}
