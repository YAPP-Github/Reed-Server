package org.yapp.infra.config.external.sentry

import io.sentry.Sentry
import io.sentry.SentryOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.yapp.infra.InfraBaseConfig

@Configuration
@EnableConfigurationProperties(SentryProperties::class)
class SentryConfig(
    private val sentryProperties: SentryProperties,
    @Value("\${app.module-name}")
    private val moduleName: String
) : InfraBaseConfig {

    @Bean
    fun sentryOptionsCustomizer(): Sentry.OptionsConfiguration<SentryOptions> {
        return Sentry.OptionsConfiguration { options: SentryOptions ->
            if (sentryProperties.dsn.isBlank()) {
                options.isEnabled = false
                return@OptionsConfiguration
            }

            options.dsn = sentryProperties.dsn
            options.environment = sentryProperties.environment
            options.serverName = sentryProperties.serverName
            options.setTag("module", moduleName)
        }
    }
}


