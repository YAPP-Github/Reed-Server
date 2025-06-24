package org.yapp.apis.auth.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.auth.dto.request.SocialLoginRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.AuthResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.usecase.AuthUseCase
import org.yapp.apis.util.AuthUtils

/**
 * Implementation of the authentication controller API.
 */
@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) : AuthControllerApi {

    override fun signIn(request: SocialLoginRequest): ResponseEntity<AuthResponse> {
        val credentials = SocialLoginRequest.toCredentials(request)
        val tokenPair = authUseCase.signIn(credentials)
        return ResponseEntity.ok(AuthResponse.fromTokenPair(tokenPair))
    }

    override fun refreshToken(request: TokenRefreshRequest): ResponseEntity<AuthResponse> {
        val tokenPair = authUseCase.refreshToken(request.refreshToken)
        return ResponseEntity.ok(AuthResponse.fromTokenPair(tokenPair))
    }

    override fun signOut(authorization: String): ResponseEntity<Void> {
        val userId = AuthUtils.extractUserIdFromAuthHeader(authorization, authUseCase::getUserIdFromAccessToken)
        authUseCase.signOut(userId)
        return ResponseEntity.noContent().build()
    }

    override fun getUserProfile(authorization: String): ResponseEntity<UserProfileResponse> {
        val userId = AuthUtils.extractUserIdFromAuthHeader(authorization, authUseCase::getUserIdFromAccessToken)
        val userProfile = authUseCase.getUserProfile(userId)
        return ResponseEntity.ok(userProfile)
    }
}
