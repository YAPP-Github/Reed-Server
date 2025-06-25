package org.yapp.redis.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import org.yapp.domain.auth.TokenRepository
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Implementation of TokenRepository using Redis.
 */
@Repository
class RedisTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>
) : TokenRepository {

    companion object {
        private const val REFRESH_TOKEN_PREFIX = "refresh_token:"
    }

    override fun saveRefreshToken(userId: UUID, refreshToken: String, expirationTimeInSeconds: Long) {
        val key = getRefreshTokenKey(userId)
        redisTemplate.opsForValue().set(key, refreshToken, expirationTimeInSeconds, TimeUnit.SECONDS)
    }

    override fun getRefreshToken(userId: UUID): String? {
        val key = getRefreshTokenKey(userId)
        return redisTemplate.opsForValue().get(key)
    }

    override fun deleteRefreshToken(userId: UUID) {
        val key = getRefreshTokenKey(userId)
        redisTemplate.delete(key)
    }

    override fun existsRefreshToken(userId: UUID, refreshToken: String): Boolean {
        val storedToken = getRefreshToken(userId)
        return storedToken != null && storedToken == refreshToken
    }

    private fun getRefreshTokenKey(userId: UUID): String {
        return "$REFRESH_TOKEN_PREFIX$userId"
    }
}
