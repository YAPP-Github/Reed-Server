package org.yapp.gateway.config

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    @field:NotBlank
    val secretKey: String,
    val accessTokenExpiration: Duration,
    val refreshTokenExpiration: Duration
)
