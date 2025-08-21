package org.yapp.apis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.kakao")
data class KakaoOauthProperties(
    val adminKey: String
) 
