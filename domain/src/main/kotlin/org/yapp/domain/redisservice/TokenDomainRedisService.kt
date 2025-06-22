package org.yapp.domain.redisservice

import java.util.*

interface TokenDomainRedisService {
    fun saveRefreshToken(userId: UUID, refreshToken: String, expiration: Long)
    fun deleteRefreshToken(userId: UUID)
    fun validateRefreshToken(userId: UUID, refreshToken: String): Boolean
}
