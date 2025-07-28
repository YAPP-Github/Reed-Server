package org.yapp.apis.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AppleOauthProperties::class)
class AuthConfig
