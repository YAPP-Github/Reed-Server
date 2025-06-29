package org.yapp.infra.config.internal.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.yapp.infra.InfraBaseConfig

@EnableTransactionManagement
@EntityScan(basePackages = ["org.yapp.infra"])
@EnableJpaRepositories(basePackages = ["org.yapp.infra"])
class JpaConfig : InfraBaseConfig
