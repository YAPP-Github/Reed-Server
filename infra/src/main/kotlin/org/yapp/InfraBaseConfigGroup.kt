package org.yapp

import org.yapp.config.internal.async.AsyncConfig
import org.yapp.config.internal.jpa.JpaConfig
import org.yapp.config.external.redis.RedisConfig

enum class InfraBaseConfigGroup(
    val configClass: Class<out InfraBaseConfig>
) {
    ASYNC(AsyncConfig::class.java),
    JPA(JpaConfig::class.java),
    REDIS(RedisConfig::class.java)
}
