package org.yapp.apis.config

import org.springframework.context.annotation.Configuration
import org.yapp.infra.EnableInfraBaseConfig
import org.yapp.infra.InfraBaseConfigGroup

@Configuration(proxyBeanMethods = false)
@EnableInfraBaseConfig(
    [
        InfraBaseConfigGroup.JPA,
        InfraBaseConfigGroup.ASYNC,
        InfraBaseConfigGroup.REDIS,
        InfraBaseConfigGroup.REST_CLIENT,
        InfraBaseConfigGroup.QUERY_DSL
    ]
)
class InfraConfig
