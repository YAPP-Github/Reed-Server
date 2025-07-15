package org.yapp.domain.user

import org.yapp.globalutils.auth.Role
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

/**
 * User domain model.
 *
 * @property id The unique identifier of the user.
 * @property email The email of the user.
 * @property nickname The nickname of the user.
 * @property profileImageUrl The URL of the user's profile image.
 * @property providerType The type of authentication provider.
 * @property providerId The ID from the authentication provider.
 * @property role The roles of the user (e.g., USER, ADMIN).
 * @property createdAt The timestamp when the user was created.
 * @property updatedAt The timestamp when the user was last updated.
 * @property deletedAt The timestamp when the user was soft-deleted, or null if the user is not deleted.
 */
data class User private constructor(
    val id: UUID,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val providerType: ProviderType,
    val providerId: String,
    val role: Role,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null
) {

    fun restore(): User {
        require(this.isDeleted()) { "User is already active" }
        return this.copy(
            deletedAt = null,
            updatedAt = LocalDateTime.now()
        )
    }

    companion object {
        fun create(
            email: String,
            nickname: String,
            profileImageUrl: String?,
            providerType: ProviderType,
            providerId: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime? = null
        ): User {
            return User(
                id = UuidGenerator.create(),
                email = email,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId,
                role = Role.USER,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }

        // 추후 다른 역할 부여 시 사용
        fun createWithRole(
            email: String,
            nickname: String,
            profileImageUrl: String?,
            providerType: ProviderType,
            providerId: String,
            role: Role,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime? = null
        ): User {
            return User(
                id = UuidGenerator.create(),
                email = email,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId,
                role = role,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }

        fun reconstruct(
            id: UUID,
            email: String,
            nickname: String,
            profileImageUrl: String?,
            providerType: ProviderType,
            providerId: String,
            role: Role,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime? = null
        ): User {
            return User(
                id = id,
                email = email,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId,
                role = role,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }
    }

    private fun isDeleted(): Boolean = deletedAt != null
}
