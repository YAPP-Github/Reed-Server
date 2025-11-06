package org.yapp.domain.token

import org.yapp.domain.user.User
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

data class RefreshToken private constructor(
    val id: Id?,
    val token: Token,
    val userId: User.Id,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime
) {
    fun isExpired(): Boolean {
        return expiresAt.isBefore(LocalDateTime.now())
    }

    companion object {
        fun create(
            token: String,
            userId: UUID,
            expiresAt: LocalDateTime,
            createdAt: LocalDateTime
        ): RefreshToken {
            return RefreshToken(
                id = Id.newInstance(UuidGenerator.create()),
                token = Token.newInstance(token),
                userId = User.Id.newInstance(userId),
                expiresAt = expiresAt,
                createdAt = createdAt
            )
        }

        fun reconstruct(
            id: Id,
            token: Token,
            userId: User.Id,
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

    @JvmInline
    value class Id(val value: UUID) {
        override fun toString(): String = value.toString()

        companion object {
            fun newInstance(value: UUID): Id {
                return Id(value)
            }
        }
    }

    @JvmInline
    value class Token(val value: String) {
        override fun toString(): String = value

        companion object {
            fun newInstance(value: String): Token {
                require(value.isNotBlank()) { "Token must not be blank" }
                return Token(value)
            }
        }
    }
}
