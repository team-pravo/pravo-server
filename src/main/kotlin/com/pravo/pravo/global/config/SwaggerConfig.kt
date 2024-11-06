package com.pravo.pravo.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val securityScheme: SecurityScheme =
            SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .`in`(SecurityScheme.In.HEADER)
                .name("Authorization")
        return OpenAPI()
            .servers(listOf(Server().url("/")))
            .components(Components().addSecuritySchemes("jwt", securityScheme))
            .info(configurationInfo())
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("Pravo API")
            .description("Pravo API documentation")
            .version("1.0.0")
    }
}
