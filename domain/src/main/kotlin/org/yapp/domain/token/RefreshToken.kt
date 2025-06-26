package org.yapp.domain.token

import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*


data class RefreshToken private constructor(
    val id: UUID?,
    val token: String,
    val userId: UUID,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(
            token: String,
            userId: UUID,
            expiresAt: LocalDateTime,
            createdAt: LocalDateTime
        ): RefreshToken {
            return RefreshToken(
                id = UuidGenerator.create(),
                token = token,
                userId = userId,
                expiresAt = expiresAt,
                createdAt = createdAt
            )
        }

        fun reconstruct(
            id: UUID,
            token: String,
            userId: UUID,
            expiresAt: LocalDateTime,
            createdAt: LocalDateTime
        ): RefreshToken {
            return RefreshToken(
                id = id,
                token = token,
                userId = userId,
                expiresAt = expiresAt,
                createdAt = createdAt
            )
        }
    }
}
