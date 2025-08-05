package org.yapp.infra.external.redis.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.yapp.domain.token.RefreshToken
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

@RedisHash(
    value = "refreshToken",
    timeToLive = 1209600
)
class RefreshTokenEntity private constructor(
    @Id
    val id: UUID,
    @Indexed
    val token: String,
    @Indexed
    val userId: UUID,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime
) {
    fun toDomain(): RefreshToken = RefreshToken.reconstruct(
        id = RefreshToken.Id.newInstance(this.id),
        token = RefreshToken.Token.newInstance(this.token),
        userId = RefreshToken.UserId.newInstance(this.userId),
        expiresAt = expiresAt,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(refreshToken: RefreshToken): RefreshTokenEntity {
            return RefreshTokenEntity(
                id = refreshToken.id?.value ?: UuidGenerator.create(),
                token = refreshToken.token.value,
                userId = refreshToken.userId.value,
                expiresAt = refreshToken.expiresAt,
                createdAt = refreshToken.createdAt
            )
        }
    }
}
