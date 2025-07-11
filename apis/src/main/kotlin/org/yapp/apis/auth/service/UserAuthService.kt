package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.UserCreateInfo
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.service.domain.UserDomainService
import org.yapp.domain.user.User
import org.yapp.domain.user.vo.SocialUserProfile
import java.util.*

@Service
class UserAuthService(
    private val userDomainService: UserDomainService
) {

    fun findUserById(userId: UUID): User {
        return userDomainService.findById(userId)
            ?: throw AuthException(AuthErrorCode.USER_NOT_FOUND, "User not found: $userId")
    }

    fun findOrCreateUser(userInfo: UserCreateInfo): User {
        // 1. providerId로 기존 유저 조회
        userDomainService.findByProviderTypeAndProviderId(userInfo.providerType, userInfo.providerId)
            ?.let { return it }

        // 2. soft-deleted 유저 조회 및 복구
        userDomainService.findByProviderTypeAndProviderIdIncludingDeleted(userInfo.providerType, userInfo.providerId)
            ?.let { return userDomainService.restoreDeletedUser(it) }

        // 3. 새 유저 생성할 때만 SocialUserProfile 생성
        return createNewUser(userInfo)
    }


    private fun createNewUser(userInfo: UserCreateInfo): User {
        val finalEmail = userInfo.email?.takeIf { it.isNotBlank() }
            ?: "${userInfo.providerId}@${userInfo.providerType.name.lowercase()}.local"

        val finalNickname = userInfo.nickname?.takeIf { it.isNotBlank() }
            ?: NicknameGenerator.generate()

        // 이메일 중복 체크 (생성 시에만)
        if (userDomainService.existsActiveByEmail(finalEmail)) {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, "Email already in use")
        }

        val profile = SocialUserProfile.newInstance(
            email = finalEmail,
            nickname = finalNickname,
            profileImageUrl = userInfo.profileImageUrl,
            providerType = userInfo.providerType,
            providerId = userInfo.providerId
        )

        return userDomainService.create(profile)
    }

    fun validateUserExists(userId: UUID) {
        if (!userDomainService.existsById(userId)) {
            throw AuthException(AuthErrorCode.USER_NOT_FOUND, "User not found: $userId")
        }
    }
}
