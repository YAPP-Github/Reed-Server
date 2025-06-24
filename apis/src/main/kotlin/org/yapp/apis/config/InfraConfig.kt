package org.yapp.apis.config

import org.springframework.context.annotation.Configuration
import org.yapp.EnableInfraBaseConfig
import org.yapp.InfraBaseConfigGroup

@Configuration(proxyBeanMethods = false)
@EnableInfraBaseConfig(
    [
        InfraBaseConfigGroup.JPA,
        InfraBaseConfigGroup.ASYNC,
        InfraBaseConfigGroup.REDIS,
    ]
)
class InfraConfig {
}
