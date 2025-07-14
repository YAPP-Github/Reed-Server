package org.yapp.domain.user

import org.yapp.domain.user.exception.UserErrorCode
import org.yapp.domain.user.exception.UserNotFoundException
import org.yapp.domain.user.vo.UserIdentity
import org.yapp.domain.user.vo.UserProfile
import org.yapp.globalutils.annotation.DomainService
import org.yapp.globalutils.util.TimeProvider
import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider
) {
    fun findUserProfileById(id: UUID): UserProfile {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserProfile.newInstance(user)
    }

    fun findUserIdentityById(id: UUID): UserIdentity {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserIdentity.newInstance(user)
    }

    fun findUserByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserIdentity? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
            ?.let { UserIdentity.newInstance(it) }
    }

    fun findUserByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): UserIdentity? {
        return userRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)
            ?.let { UserIdentity.newInstance(it) }
    }

    fun existsActiveUserByIdAndDeletedAtIsNull(userId: UUID): Boolean {
        return userRepository.existsByIdAndDeletedAtIsNull(userId)
    }

    fun existsActiveUserByEmailAndDeletedAtIsNull(email: String): Boolean {
        return userRepository.existsByEmailAndDeletedAtIsNull(email)
    }

    fun create(
        email: String,
        nickname: String,
        profileImageUrl: String?,
        providerType: ProviderType,
        providerId: String
    ): UserIdentity {
        val now = timeProvider.now()
        val user = User.create(
            email = email,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            providerType = providerType,
            providerId = providerId,
            createdAt = now,
            updatedAt = now
        )
        val savedUser = userRepository.save(user)
        return UserIdentity.newInstance(savedUser)
    }

    fun restoreDeletedUser(userId: UUID): UserIdentity {
        val deletedUser = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val restoredUser = userRepository.save(deletedUser.restore())
        return UserIdentity.newInstance(restoredUser)
    }
}
