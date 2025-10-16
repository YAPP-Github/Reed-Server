package org.yapp.batch.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
    "org.yapp.infra.notification.repository.impl",
    "org.yapp.infra.user.repository.impl"
])
class RepositoryConfig {
    // This configuration enables component scanning for repository implementations
    // from the infra module, making them available for autowiring in the batch module
}
