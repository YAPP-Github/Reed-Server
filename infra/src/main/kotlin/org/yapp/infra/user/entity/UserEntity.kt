package org.yapp.infra.user.entity


import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.common.BaseTimeEntity
import org.yapp.domain.user.User
import org.yapp.global.util.UuidGenerator
import java.sql.Types
import java.util.*

@Entity
@Table(name = "users")
class UserEntity private constructor(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID = UuidGenerator.create(),

    @Column(nullable = false, length = 100)
    val email: String,

    nickname: String,
    profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 20)
    val providerType: ProviderType,

    @Column(name = "provider_id", nullable = false, length = 100)
    val providerId: String
) : BaseTimeEntity() {

    @Column(nullable = false, length = 100)
    var nickname: String = nickname
        protected set

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = profileImageUrl
        protected set

    fun toDomain(): User = User.reconstruct(
        id = id,
        email = email,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        providerType = providerType,
        providerId = providerId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id ?: UuidGenerator.create(),
            email = user.email,
            nickname = user.nickname,
            profileImageUrl = user.profileImageUrl,
            providerType = user.providerType,
            providerId = user.providerId
        ).apply {
            this.createdAt = user.createdAt
            this.updatedAt = user.updatedAt
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
