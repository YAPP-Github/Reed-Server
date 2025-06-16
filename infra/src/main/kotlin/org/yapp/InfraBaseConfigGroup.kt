package org.yapp

import org.yapp.async.AsyncConfig
import org.yapp.jpa.JpaConfig

enum class InfraBaseConfigGroup(
    val configClass: Class<out InfraBaseConfig>
) {
    ASYNC(AsyncConfig::class.java),
    JPA(JpaConfig::class.java)
}
