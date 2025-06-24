package org.yapp.domain.user

import org.yapp.domain.auth.ProviderType
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
 * @property createdAt The timestamp when the user was created.
 * @property updatedAt The timestamp when the user was last updated.
 */
data class User(
    val id: UUID? = null,
    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,
    val providerType: ProviderType,
    val providerId: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
