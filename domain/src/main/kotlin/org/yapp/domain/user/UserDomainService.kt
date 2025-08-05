package org.yapp.domain.user

import org.yapp.domain.user.exception.UserErrorCode
import org.yapp.domain.user.exception.UserNotFoundException
import org.yapp.domain.user.vo.UserAuthVO
import org.yapp.domain.user.vo.UserIdentityVO
import org.yapp.domain.user.vo.UserProfileVO
import org.yapp.globalutils.annotation.DomainService

import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository,
) {
    fun findUserProfileById(id: UUID): UserProfileVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserProfileVO.newInstance(user)
    }

    fun findUserIdentityById(id: UUID): UserIdentityVO {
        val user = userRepository.findById(id) ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)
        return UserIdentityVO.newInstance(user)
    }

    fun findUserByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserAuthVO? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
            ?.let { UserAuthVO.newInstance(it) }
    }

    fun findUserByProviderTypeAndProviderIdIncludingDeleted(
        providerType: ProviderType,
        providerId: String
    ): UserAuthVO? {
        return userRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)
            ?.let { UserAuthVO.newInstance(it) }
    }

    fun existsActiveUserByIdAndDeletedAtIsNull(id: UUID): Boolean {
        return userRepository.existsById(id)
    }

    fun existsActiveUserByEmailAndDeletedAtIsNull(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun createUser(
        email: String,
        nickname: String,
        profileImageUrl: String?,
        providerType: ProviderType,
        providerId: String
    ): UserAuthVO {
        val user = User.create(
            email = email,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            providerType = providerType,
            providerId = providerId
        )
        val savedUser = userRepository.save(user)
        return UserAuthVO.newInstance(savedUser)
    }

    fun restoreDeletedUser(userId: UUID): UserAuthVO {
        val deletedUser = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val restoredUser = userRepository.save(deletedUser.restore())
        return UserAuthVO.newInstance(restoredUser)
    }

    fun updateTermsAgreement(userId: UUID, termsAgreed: Boolean): UserProfileVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateTermsAgreement(termsAgreed))
        return UserProfileVO.newInstance(updatedUser)
    }

    fun updateAppleRefreshToken(userId: UUID, refreshToken: String): UserIdentityVO {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUND)

        val updatedUser = userRepository.save(user.updateAppleRefreshToken(refreshToken))

        return UserIdentityVO.newInstance(updatedUser)
    }
}
