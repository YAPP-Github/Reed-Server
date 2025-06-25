package org.yapp.infra

import org.yapp.infra.config.internal.async.AsyncConfig
import org.yapp.infra.config.internal.jpa.JpaConfig
import org.yapp.infra.config.external.redis.RedisConfig

enum class InfraBaseConfigGroup(
    val configClass: Class<out InfraBaseConfig>
) {
    ASYNC(AsyncConfig::class.java),
    JPA(JpaConfig::class.java),
    REDIS(RedisConfig::class.java)
}
