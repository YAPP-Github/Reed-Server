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
        val profile = SocialUserProfile.newInstance(
            email = userInfo.email,
            nickname = userInfo.nickname,
            profileImageUrl = userInfo.profileImageUrl,
            providerType = userInfo.providerType,
            providerId = userInfo.providerId // providerId
        )

        // 1. 활성 유저가 있는 경우 즉시 리턴
        userDomainService.findByProvider(profile)
            ?.let { return it }

        // 2. Soft-deleted 유저가 있다면 복구 후 리턴
        userDomainService.findDeletedByProvider(profile)
            ?.let { return userDomainService.restore(it) }

        // 3. 이메일이 활성 유저에게 사용 중이라면 예외
        if (userDomainService.existsActiveByEmail(profile.email)) {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, "Email already in use")
        }

        // 4. 이메일이 다른 provider의 soft-deleted 유저에게 사용 중이면 예외
        if (userDomainService.existsDeletedByEmailWithDifferentProvider(profile)) {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, "Email already in use by a deleted account")
        }

        // 5. 새 유저 생성 후 저장
        return userDomainService.create(profile)
    }
}
