package org.yapp.batch.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = [
    "org.yapp.infra.notification.entity",
    "org.yapp.infra.user.entity"
])
@EnableJpaRepositories(basePackages = [
    "org.yapp.infra.notification.repository",
    "org.yapp.infra.user.repository"
])
class DataJpaConfig {
    // This configuration enables JPA repositories and entity scanning
    // from the infra module, making them available for autowiring in the batch module
}
