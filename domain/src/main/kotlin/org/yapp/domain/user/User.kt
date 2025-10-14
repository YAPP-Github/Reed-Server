package org.yapp.domain.user

import org.yapp.globalutils.auth.Role
import org.yapp.globalutils.util.UuidGenerator
import org.yapp.globalutils.validator.EmailValidator
import java.time.LocalDateTime
import java.util.*

data class User private constructor(
    val id: Id,
    val email: Email,
    val nickname: String,
    val profileImageUrl: String?,
    val providerType: ProviderType,
    val providerId: ProviderId,
    val role: Role,
    val termsAgreed: Boolean = false,
    val appleRefreshToken: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
    val lastActivity: LocalDateTime? = null,
    val notificationEnabled: Boolean = true,
) {

    fun restore(): User {
        require(this.isDeleted()) { "User is already active" }
        return this.copy(
            deletedAt = null
        )
    }

    fun updateTermsAgreement(termsAgreed: Boolean): User {
        return this.copy(
            termsAgreed = termsAgreed
        )
    }

    fun updateAppleRefreshToken(token: String): User {
        return this.copy(
            appleRefreshToken = token
        )
    }

    fun updateLastActivity(): User {
        return this.copy(
            lastActivity = LocalDateTime.now()
        )
    }

    fun updateNotificationSettings(notificationEnabled: Boolean): User {
        return this.copy(
            notificationEnabled = notificationEnabled
        )
    }

    companion object {
        fun create(
            email: String,
            nickname: String,
            profileImageUrl: String?,
            providerType: ProviderType,
            providerId: String,
            termsAgreed: Boolean = false
        ): User {
            return User(
                id = Id.newInstance(UuidGenerator.create()),
                email = Email.newInstance(email),
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = ProviderId.newInstance(providerId),
                role = Role.USER,
                termsAgreed = termsAgreed,
                appleRefreshToken = null,
                lastActivity = LocalDateTime.now()
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
            termsAgreed: Boolean = false
        ): User {
            return User(
                id = Id.newInstance(UuidGenerator.create()),
                email = Email.newInstance(email),
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = ProviderId.newInstance(providerId),
                role = role,
                termsAgreed = termsAgreed,
                appleRefreshToken = null,
                lastActivity = LocalDateTime.now()
            )
        }

        fun reconstruct(
            id: Id,
            email: Email,
            nickname: String,
            profileImageUrl: String?,
            providerType: ProviderType,
            providerId: ProviderId,
            role: Role,
            termsAgreed: Boolean = false,
            appleRefreshToken: String? = null,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null,
            lastActivity: LocalDateTime? = null,
            notificationEnabled: Boolean = true
        ): User {
            return User(
                id = id,
                email = email,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId,
                role = role,
                termsAgreed = termsAgreed,
                appleRefreshToken = appleRefreshToken,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
                lastActivity = lastActivity,
                notificationEnabled = notificationEnabled
            )
        }
    }

    private fun isDeleted(): Boolean = deletedAt != null

    @JvmInline
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }

    @JvmInline
    value class Email(val value: String) {
        companion object {
            fun newInstance(value: String): Email {
                require(EmailValidator.isValidEmail(value)) { "This is not a valid email format." }
                return Email(value)
            }
        }
    }

    @JvmInline
    value class ProviderId(val value: String) {
        companion object {
            fun newInstance(value: String): ProviderId {
                require(value.isNotBlank()) { "Provider ID는 필수입니다." }
                return ProviderId(value)
            }
        }
    }
}
