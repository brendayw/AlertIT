package com.example.AlertIT.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AlertIT")
                        .description("AlertIT es un servicio de notificaciones meteorológicas que detecta condiciones " +
                                "de lluvia o viento fuertes y envía alertas automáticamente por WhatsApp."));
    }

}
