package org.yapp.apis.user.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.user.dto.request.FcmTokenRequest
import org.yapp.apis.user.dto.request.NotificationSettingsRequest
import org.yapp.apis.user.dto.request.TermsAgreementRequest
import org.yapp.apis.user.dto.response.UserProfileResponse
import org.yapp.apis.user.service.UserService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class UserUseCase(
    private val userService: UserService
) {
    fun getUserProfile(userId: UUID): UserProfileResponse {
        return userService.findUserProfileByUserId(userId)
    }

    @Transactional
    fun updateTermsAgreement(userId: UUID, request: TermsAgreementRequest): UserProfileResponse {
        return userService.updateTermsAgreement(userId, request)
    }

    @Transactional
    fun updateNotificationSettings(userId: UUID, request: NotificationSettingsRequest): UserProfileResponse {
        return userService.updateNotificationSettings(userId, request)
    }

    @Transactional
    fun updateFcmToken(userId: UUID, request: FcmTokenRequest): UserProfileResponse {
        return userService.updateFcmToken(userId, request)
    }
}
