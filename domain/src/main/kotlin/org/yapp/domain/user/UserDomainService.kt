package org.yapp.domain.user

import org.yapp.domain.user.exception.UserErrorCode
import org.yapp.domain.user.exception.UserNotFoundException
import org.yapp.domain.user.vo.UserIdentityVO
import org.yapp.domain.user.vo.UserProfileVO
import org.yapp.globalutils.annotation.DomainService
import org.yapp.globalutils.util.TimeProvider
import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider
) {
    fun findUserProfileById(id: UUID): UserProfileVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserProfileVO.newInstance(user)
    }

    fun findUserIdentityById(id: UUID): UserIdentityVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserIdentityVO.newInstance(user)
    }

    fun findUserByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserIdentityVO? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
            ?.let { UserIdentityVO.newInstance(it) }
    }

    fun findUserByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): UserIdentityVO? {
        return userRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)
            ?.let { UserIdentityVO.newInstance(it) }
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
    ): UserIdentityVO {
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
        return UserIdentityVO.newInstance(savedUser)
    }

    fun restoreDeletedUser(userId: UUID): UserIdentityVO {
        val deletedUser = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val restoredUser = userRepository.save(deletedUser.restore())
        return UserIdentityVO.newInstance(restoredUser)
    }
}
