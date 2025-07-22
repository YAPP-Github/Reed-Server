package org.yapp.infra

import org.yapp.infra.config.external.api.RestClientConfig
import org.yapp.infra.config.external.redis.RedisConfig
import org.yapp.infra.config.internal.async.AsyncConfig
import org.yapp.infra.config.internal.jpa.JpaConfig
import org.yapp.infra.config.internal.page.PageConfig
import org.yapp.infra.config.internal.querydsl.QuerydslConfig

enum class InfraBaseConfigGroup(
    val configClass: Class<out InfraBaseConfig>
) {
    ASYNC(AsyncConfig::class.java),
    JPA(JpaConfig::class.java),
    PAGE(PageConfig::class.java),
    REDIS(RedisConfig::class.java),
    REST_CLIENT(RestClientConfig::class.java),
    QUERY_DSL(QuerydslConfig::class.java)
}
