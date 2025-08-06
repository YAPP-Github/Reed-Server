package org.yapp.apis.auth.service

import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.SaveAppleRefreshTokenRequest
import org.yapp.apis.auth.strategy.signin.AppleAuthCredentials
import org.yapp.apis.auth.strategy.signin.SignInCredentials
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.user.service.UserAccountService

@Service
class UserSignInService(
    private val userAccountService: UserAccountService,
    private val appleAuthService: AppleAuthService
) {
    @Transactional
    fun processSignIn(
        @Valid request: FindOrCreateUserRequest,
        credentials: SignInCredentials
    ): CreateUserResponse {
        val createUserResponse = userAccountService.findOrCreateUser(request)
        handleAppleRefreshTokenIfNeeded(createUserResponse, credentials)

        return createUserResponse
    }

    private fun handleAppleRefreshTokenIfNeeded(
        createUserResponse: CreateUserResponse,
        credentials: SignInCredentials
    ) {
        if (credentials is AppleAuthCredentials) {
            val request = SaveAppleRefreshTokenRequest.of(createUserResponse, credentials)
            appleAuthService.saveAppleRefreshTokenIfMissing(request)
        }
    }
}
