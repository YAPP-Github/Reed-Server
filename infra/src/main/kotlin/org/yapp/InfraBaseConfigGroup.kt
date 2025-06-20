package org.yapp

import org.yapp.async.AsyncConfig
import org.yapp.jpa.JpaConfig
import org.yapp.redis.RedisConfig

enum class InfraBaseConfigGroup(
    val configClass: Class<out InfraBaseConfig>
) {
    ASYNC(AsyncConfig::class.java),
    JPA(JpaConfig::class.java),
    REDIS(RedisConfig::class.java)
}
