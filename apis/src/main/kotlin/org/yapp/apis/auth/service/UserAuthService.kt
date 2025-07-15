package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.request.FindOrCreateUserRequest
import org.yapp.apis.auth.dto.request.FindUserIdentityRequest
import org.yapp.apis.auth.dto.response.CreateUserResponse
import org.yapp.apis.auth.dto.response.UserAuthInfoResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.user.UserDomainService
import org.yapp.domain.user.vo.UserIdentityVO
import java.util.*

@Service
class UserAuthService(
    private val userDomainService: UserDomainService
) {
    fun findUserProfileByUserId(userId: UUID): UserProfileResponse {
        val userProfile = userDomainService.findUserProfileById(userId)
        return UserProfileResponse.from(userProfile)
    }

    fun validateUserExists(userId: UUID) {
        if (!userDomainService.existsActiveUserByIdAndDeletedAtIsNull(userId)) {
            throw AuthException(AuthErrorCode.USER_NOT_FOUND, "User not found: $userId")
        }
    }

    fun findUserIdentityByUserId(findUserIdentityRequest: FindUserIdentityRequest): UserAuthInfoResponse {
        val userIdentity = userDomainService.findUserIdentityById(findUserIdentityRequest.validUserId())
        return UserAuthInfoResponse.from(userIdentity)
    }

    fun findOrCreateUser(findOrCreateUserRequest: FindOrCreateUserRequest): CreateUserResponse {
        userDomainService.findUserByProviderTypeAndProviderId(
            findOrCreateUserRequest.validProviderType(),
            findOrCreateUserRequest.validProviderId()
        )?.let { return CreateUserResponse.from(it) }

        userDomainService.findUserByProviderTypeAndProviderIdIncludingDeleted(
            findOrCreateUserRequest.validProviderType(),
            findOrCreateUserRequest.validProviderId()
        )?.let { deletedUserIdentity ->
            return CreateUserResponse.from(
                userDomainService.restoreDeletedUser(deletedUserIdentity.id.value)
            )
        }

        val createdUser = createNewUser(findOrCreateUserRequest)
        return CreateUserResponse.from(createdUser)
    }

    private fun createNewUser(findOrCreateUserRequest: FindOrCreateUserRequest): UserIdentityVO {
        val email = findOrCreateUserRequest.getOrDefaultEmail()
        val nickname = findOrCreateUserRequest.getOrDefaultNickname()

        if (userDomainService.existsActiveUserByEmailAndDeletedAtIsNull(email)) {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, "Email already in use")
        }

        return userDomainService.createNewUser(
            email = email,
            nickname = nickname,
            profileImageUrl = findOrCreateUserRequest.profileImageUrl,
            providerType = findOrCreateUserRequest.validProviderType(),
            providerId = findOrCreateUserRequest.validProviderId()
        )
    }
}
