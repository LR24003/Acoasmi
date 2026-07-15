package com.acoasmi.roble.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API para el Sistema Informático de La Asociación Cooperativa El Roble de R.L.",
        version = "1.0.0",
        description = """
            API REST para la intermediación financiera de La Asociación Cooperativa El Roble de R.L..
            
            ## Características principales:
            * CRUD completo de Créditos, ahorros y aportaciones
            * Búsqueda de Asociados por nombre y DUI.
            * Manejo de Perfiles de riesgo según la UIF
            * Documentación interactiva con Swagger UI
            
            ## Desarrollado por:
             
            Ing. José Alfredo López Rivera - LR24003  
            
            
            ## Repositorio del proyecto
            https://github.com/LR24003/Acoasmi
            """,
        contact = @Contact(
            name = "Alfredo López",
            email = "josepalfred7@gmail.com"
        ),
        license = @License(
            name = "Ingeniero en Desarrollo de Software",
            url = "https://github.com/LR24003/Acoasmi"
        )
    ),
    servers = {
        @Server(
            description = "Servidor Web y Local",
            url = "http://localhost:8080"
        )
    }
)

public class OpenApiConfig {
}