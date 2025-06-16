package org.yapp.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.yapp.InfraBaseConfig

@EnableTransactionManagement
@EntityScan("org.yapp")
@EnableJpaRepositories("org.yapp")
class JpaConfig : InfraBaseConfig {

}
