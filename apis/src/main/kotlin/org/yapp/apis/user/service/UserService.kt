package org.yapp.apis.user.service

import jakarta.validation.Valid
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.user.dto.request.FcmTokenRequest
import org.yapp.apis.user.dto.request.FindUserIdentityRequest
import org.yapp.apis.user.dto.request.NotificationSettingsRequest
import org.yapp.apis.user.dto.request.TermsAgreementRequest
import org.yapp.apis.user.dto.response.UserAuthInfoResponse
import org.yapp.apis.user.dto.response.UserProfileResponse
import org.yapp.domain.user.UserDomainService
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
class UserService(
    private val userDomainService: UserDomainService
) {
    fun findUserProfileByUserId(userId: UUID): UserProfileResponse {
        val userProfile = userDomainService.findUserProfileById(userId)
        return UserProfileResponse.from(userProfile)
    }

    fun updateTermsAgreement(userId: UUID, @Valid request: TermsAgreementRequest): UserProfileResponse {
        validateUserExists(userId)
        val updatedUserProfile = userDomainService.updateTermsAgreement(userId, request.validTermsAgreed())
        return UserProfileResponse.from(updatedUserProfile)
    }

    fun validateUserExists(userId: UUID) {
        if (!userDomainService.existsActiveUserByIdAndDeletedAtIsNull(userId)) {
            throw AuthException(AuthErrorCode.USER_NOT_FOUND, "User not found: $userId")
        }
    }

    fun findUserIdentityByUserId(@Valid findUserIdentityRequest: FindUserIdentityRequest): UserAuthInfoResponse {
        val userIdentity = userDomainService.findUserIdentityById(findUserIdentityRequest.validUserId())
        return UserAuthInfoResponse.from(userIdentity)
    }

    fun updateNotificationSettings(userId: UUID, @Valid request: NotificationSettingsRequest): UserProfileResponse {
        validateUserExists(userId)
        val updatedUserProfile = userDomainService.updateNotificationSettings(userId, request.validNotificationEnabled())
        return UserProfileResponse.from(updatedUserProfile)
    }

    fun updateFcmToken(userId: UUID, @Valid request: FcmTokenRequest): UserProfileResponse {
        validateUserExists(userId)
        val updatedUserProfile = userDomainService.updateFcmToken(userId, request.validFcmToken())
        return UserProfileResponse.from(updatedUserProfile)
    }
}
