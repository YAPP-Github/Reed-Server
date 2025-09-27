package org.yapp.infra.config.external.sentry

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "sentry")
data class SentryProperties(
    val dsn: String,
    val environment: String,
    val serverName: String,
)
