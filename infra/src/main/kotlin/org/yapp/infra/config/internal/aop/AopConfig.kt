package org.yapp.infra.config.internal.aop

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.yapp.infra.InfraBaseConfig
import org.yapp.infra.aop.properties.LoggingAopProperties

@Configuration
@EnableConfigurationProperties(LoggingAopProperties::class)
class AopConfig : InfraBaseConfig
