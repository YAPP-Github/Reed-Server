package org.yapp.apis.auth.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authUseCase: AuthUseCase
) : AuthControllerApi {

    @PostMapping("/signin")
    override fun signIn(@RequestBody @Valid request: SocialLoginRequest): ResponseEntity<AuthResponse> {
        val credentials = SocialLoginRequest.toCredentials(request)
        val tokenPair = authUseCase.signIn(credentials)
        return ResponseEntity.ok(AuthResponse.fromTokenPair(tokenPair))
    }

    @PostMapping("/refresh")
    override fun refreshToken(@RequestBody @Valid request: TokenRefreshRequest): ResponseEntity<AuthResponse> {
        val tokenPair = authUseCase.refreshToken(request.validRefreshToken())
        return ResponseEntity.ok(AuthResponse.fromTokenPair(tokenPair))
    }

    @PostMapping("/signout")
    override fun signOut(@RequestHeader("Authorization") authorization: String): ResponseEntity<Unit> {
        val userId = AuthUtils.extractUserIdFromAuthHeader(authorization, authUseCase::getUserIdFromAccessToken)
        authUseCase.signOut(userId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/me")
    override fun getUserProfile(@RequestHeader("Authorization") authorization: String): ResponseEntity<UserProfileResponse> {
        val userId = AuthUtils.extractUserIdFromAuthHeader(authorization, authUseCase::getUserIdFromAccessToken)
        val userProfile = authUseCase.getUserProfile(userId)
        return ResponseEntity.ok(userProfile)
    }
}
