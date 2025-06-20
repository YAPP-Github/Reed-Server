package org.yapp.domain.user

import jakarta.persistence.*
import org.yapp.domain.auth.ProviderType
import java.time.LocalDateTime

/**
 * Entity class for User.
 */
@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val email: String,

    @Column(nullable = false, length = 100)
    val nickname: String,

    @Column(name = "profile_image_url")
    val profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 20)
    val providerType: ProviderType,

    @Column(name = "provider_id", nullable = false, length = 100)
    val providerId: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): User {
        return User(
            id = id,
            email = email,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            providerType = providerType,
            providerId = providerId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomain(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                email = user.email,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl,
                providerType = user.providerType,
                providerId = user.providerId,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}