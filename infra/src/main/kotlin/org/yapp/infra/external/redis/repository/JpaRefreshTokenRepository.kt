package org.yapp.infra.external.redis.repository

import org.springframework.data.repository.CrudRepository
import org.yapp.infra.external.redis.entity.RefreshTokenEntity
import java.util.*

interface JpaRefreshTokenRepository : CrudRepository<RefreshTokenEntity, UUID> {
    fun findByUserId(userId: UUID): RefreshTokenEntity?

    fun findByToken(token: String): RefreshTokenEntity?

    fun deleteByToken(token: String)
}
