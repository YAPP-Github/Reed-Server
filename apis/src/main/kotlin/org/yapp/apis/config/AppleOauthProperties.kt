package org.yapp.apis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.apple")
data class AppleOauthProperties(
    val clientId: String,
    val keyId: String,
    val teamId: String,
    val keyPath: String,
    val audience: String
)
