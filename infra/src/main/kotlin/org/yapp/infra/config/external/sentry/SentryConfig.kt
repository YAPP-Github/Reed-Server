package org.yapp.infra.config.external.sentry

import io.sentry.Sentry
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.yapp.infra.InfraBaseConfig

@Configuration
@EnableConfigurationProperties(SentryProperties::class)
class SentryConfig(
    private val sentryProperties: SentryProperties,
    @Value("\${app.module-name}")
    private val moduleName: String
) : InfraBaseConfig {

    @PostConstruct
    fun initSentry() {
        if (sentryProperties.dsn.isNotEmpty()) {
            Sentry.init { options ->
                options.dsn = sentryProperties.dsn
                options.environment = sentryProperties.environment
                options.serverName = sentryProperties.serverName
                
                options.setTag("module", moduleName)
            }
        }
    }
}
