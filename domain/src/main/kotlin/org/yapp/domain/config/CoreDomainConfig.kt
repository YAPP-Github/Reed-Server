package org.yapp.domain.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan("org.yapp")
@EnableJpaRepositories("org.yapp")
class CoreDomainConfig {
}
