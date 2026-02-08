package org.yapp.apis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.google")
data class GoogleOauthProperties(
    val url: Url,
    val clientId: String,
    val clientSecret: String? = null,
    val grantType: String? = null,
    val redirectUri: String? = null
)

data class Url(
    val userInfo: String,
    val tokenUri: String? = null
)