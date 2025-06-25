package org.yapp.admin.config

import org.springframework.context.annotation.Configuration
import org.yapp.infra.EnableInfraBaseConfig
import org.yapp.infra.InfraBaseConfigGroup

@Configuration(proxyBeanMethods = false)
@EnableInfraBaseConfig([InfraBaseConfigGroup.JPA])
class InfraConfig
