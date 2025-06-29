package org.yapp.infra.config.external.oauth

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.yapp.infra.InfraBaseConfig

@Configuration
@EnableFeignClients(basePackages = ["org.yapp.infra.external.oauth"])
class FeignConfig : InfraBaseConfig
