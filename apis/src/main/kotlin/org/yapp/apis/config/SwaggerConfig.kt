package org.yapp.apis.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EnableConfigurationProperties(SwaggerProperties::class)
@Profile("dev")
class SwaggerConfig(
    private val swaggerProperties: SwaggerProperties
) {
    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        val servers = swaggerProperties.servers.map { serverConfig ->
            Server()
                .url(serverConfig.url)
                .description(serverConfig.description)
        }

        return OpenAPI()
            .servers(servers)
            .info(
                Info()
                    .title(swaggerProperties.info.title)
                    .description(swaggerProperties.info.description)
                    .version(swaggerProperties.info.version)
            )
            .addSecurityItem(
                SecurityRequirement().addList(securitySchemeName)
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }
}
