package org.yapp.apis.auth.service

import jakarta.validation.Valid
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.request.SaveAppleRefreshTokenRequest
import org.yapp.apis.user.dto.request.SaveGoogleRefreshTokenRequest
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.user.service.UserAccountService
import org.yapp.globalutils.annotation.ApplicationService

@ApplicationService
class UserSignInService(
    private val userAccountService: UserAccountService,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun processSignIn(
        @Valid request: FindOrCreateUserRequest, 
        appleRefreshToken: String?,
        googleRefreshToken: String?
    ): CreateUserResponse {
        val initialUserResponse = userAccountService.findOrCreateUser(request)

        var userResponse = initialUserResponse

        // Update Apple refresh token if provided
        if (!appleRefreshToken.isNullOrBlank()) {
            userResponse = userAccountService.updateAppleRefreshToken(
                SaveAppleRefreshTokenRequest.of(
                    userResponse, appleRefreshToken
                )
            )
        }

        // Update Google refresh token if provided
        if (!googleRefreshToken.isNullOrBlank()) {
            userResponse = userAccountService.updateGoogleRefreshToken(
                SaveGoogleRefreshTokenRequest.of(
                    userResponse, googleRefreshToken
                )
            )
        }

        return userResponse
    }
}
