package org.yapp.config.internal.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.yapp.InfraBaseConfig

@EnableTransactionManagement
@EntityScan(basePackages = ["org.yapp"])
@EnableJpaRepositories(basePackages = ["org.yapp"])
class JpaConfig : InfraBaseConfig
