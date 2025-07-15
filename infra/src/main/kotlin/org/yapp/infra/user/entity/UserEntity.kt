package org.yapp.infra.user.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.yapp.domain.common.BaseTimeEntity
import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.User
import org.yapp.globalutils.auth.Role
import java.sql.Types
import java.util.*

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class UserEntity private constructor(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(nullable = false, length = 100)
    val email: String,

    nickname: String,

    profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 20)
    val providerType: ProviderType,

    @Column(name = "provider_id", nullable = false, length = 100)
    val providerId: String,

    role: Role
) : BaseTimeEntity() {

    @Column(nullable = false, length = 100)
    var nickname: String = nickname
        protected set

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = profileImageUrl
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = role
        protected set

    fun toDomain(): User = User.reconstruct(
        id = User.Id.newInstance(this.id),
        email = User.Email.newInstance(this.email),
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        providerType = providerType,
        providerId = User.ProviderId.newInstance(this.providerId),
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )

    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id.value,
            email = user.email.value,
            nickname = user.nickname,
            profileImageUrl = user.profileImageUrl,
            providerType = user.providerType,
            providerId = user.providerId.value,
            role = user.role
        ).apply {
            this.createdAt = user.createdAt
            this.updatedAt = user.updatedAt
            this.deletedAt = user.deletedAt
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
