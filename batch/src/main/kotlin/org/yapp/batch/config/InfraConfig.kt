package org.yapp.batch.config

import org.springframework.context.annotation.Configuration
import org.yapp.infra.EnableInfraBaseConfig
import org.yapp.infra.InfraBaseConfigGroup

@Configuration(proxyBeanMethods = false)
@EnableInfraBaseConfig(
    [
        InfraBaseConfigGroup.JPA,
        InfraBaseConfigGroup.AOP,
        InfraBaseConfigGroup.SENTRY
    ]
)
class InfraConfig
