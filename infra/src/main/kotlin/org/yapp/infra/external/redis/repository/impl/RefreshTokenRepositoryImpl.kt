package org.yapp.infra.external.redis.repository.impl

import org.springframework.stereotype.Repository
import org.yapp.domain.token.RefreshToken
import org.yapp.domain.token.RefreshTokenRepository
import org.yapp.infra.external.redis.entity.RefreshTokenEntity
import org.yapp.infra.external.redis.repository.JpaRefreshTokenRepository
import java.util.*


@Repository
class RefreshTokenRepositoryImpl(
    private val jpaRefreshTokenRepository: JpaRefreshTokenRepository
) : RefreshTokenRepository {

    override fun findByUserId(userId: UUID): RefreshToken? {
        return jpaRefreshTokenRepository.findByUserId(userId)?.toDomain()
    }

    override fun findByToken(token: String): RefreshToken? {
        return jpaRefreshTokenRepository.findByToken(token)?.toDomain()
    }

    override fun deleteByToken(token: String) {
        jpaRefreshTokenRepository.deleteByToken(token)
    }

    override fun save(refreshToken: RefreshToken): RefreshToken {
        val entity = RefreshTokenEntity.fromDomain(refreshToken)
        val savedEntity = jpaRefreshTokenRepository.save(entity)
        return savedEntity.toDomain()
    }
}
