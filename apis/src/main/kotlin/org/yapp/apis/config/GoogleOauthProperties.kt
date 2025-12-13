package org.yapp.apis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.google")
data class GoogleOauthProperties(
    val url: Url
)

data class Url(
    val userInfo: String
)
