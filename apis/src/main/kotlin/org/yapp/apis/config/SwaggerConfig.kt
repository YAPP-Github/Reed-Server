package org.yapp.apis.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.Order
import org.yapp.globalutils.annotation.DisableSwaggerSecurity

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

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun securityOperationCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            val method = handlerMethod.method
            val hasOnMethod = AnnotatedElementUtils
                .findMergedAnnotation(method, DisableSwaggerSecurity::class.java) != null

            val hasOnInterface = if (!hasOnMethod) {
                handlerMethod.beanType.interfaces
                    .flatMap { it.methods.asIterable() }
                    .any { ifaceMethod ->
                        ifaceMethod.name == method.name &&
                                ifaceMethod.parameterTypes.contentEquals(method.parameterTypes) &&
                                ifaceMethod.isAnnotationPresent(DisableSwaggerSecurity::class.java)
                    }
            } else false

            if (hasOnMethod || hasOnInterface) {
                operation.security = emptyList()
            }

            operation
        }
    }
}
