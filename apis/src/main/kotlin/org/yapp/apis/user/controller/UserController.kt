package org.yapp.apis.user.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.yapp.apis.user.dto.request.TermsAgreementRequest
import org.yapp.apis.user.dto.response.UserProfileResponse
import org.yapp.apis.user.usecase.UserUseCase
import java.util.*

import org.yapp.apis.user.dto.request.NotificationSettingsRequest

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userUseCase: UserUseCase
) : UserControllerApi {
    @GetMapping("/me")
    override fun getUserProfile(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<UserProfileResponse> {
        val userProfile = userUseCase.getUserProfile(userId)
        return ResponseEntity.ok(userProfile)
    }

    @PutMapping("/me/terms-agreement")
    override fun updateTermsAgreement(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody request: TermsAgreementRequest
    ): ResponseEntity<UserProfileResponse> {
        val userProfile = userUseCase.updateTermsAgreement(userId, request)
        return ResponseEntity.ok(userProfile)
    }

    @PutMapping("/me/last-activity")
    override fun updateLastActivity(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<Unit> {
        userUseCase.updateLastActivity(userId)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/me/notification-settings")
    override fun updateNotificationSettings(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody request: NotificationSettingsRequest
    ): ResponseEntity<UserProfileResponse> {
        val userProfile = userUseCase.updateNotificationSettings(userId, request)
        return ResponseEntity.ok(userProfile)
    }
}
