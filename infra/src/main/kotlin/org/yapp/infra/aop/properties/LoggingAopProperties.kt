package org.yapp.infra.aop.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "logging.aop")
data class LoggingAopProperties(
    val service: ServiceLoggingProperties = ServiceLoggingProperties(),
    val controller: ControllerLoggingProperties = ControllerLoggingProperties()
) {
    data class ServiceLoggingProperties(
        val enabled: Boolean = true,
        val maxLogLength: Int = 1000,
        val sensitiveFields: Set<String> = setOf(
            "refreshToken",
            "oauthToken",
            "authorizationCode",
            "providerId",
            "accessToken"
        )
    )

    data class ControllerLoggingProperties(
        val enabled: Boolean = true
    )
}
