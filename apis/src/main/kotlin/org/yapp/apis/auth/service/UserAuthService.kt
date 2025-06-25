package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.UserCreateInfo
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
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
        val socialUserProfile = SocialUserProfile.newInstance(
            email = userInfo.email,
            nickname = userInfo.nickname,
            profileImageUrl = userInfo.profileImageUrl,
            providerType = userInfo.providerType,
            providerId = userInfo.providerId
        )

        return userDomainService.findOrCreate(socialUserProfile).getOrElse {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, it.message)
        }
    }
}
