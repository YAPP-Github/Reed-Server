package org.yapp.apis.auth.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yapp.apis.auth.dto.AuthDto
import org.yapp.apis.auth.dto.UserProfileResponse
import org.yapp.apis.auth.usecase.AuthUseCase
import org.yapp.apis.util.AuthUtils

/**
 * Implementation of the authentication controller API.
 */
@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) : AuthControllerApi {

    @PostMapping("/signin")
    override fun signIn(@RequestBody request: AuthDto.SocialLoginRequest): ResponseEntity<AuthDto.AuthResponse> {
        val credentials = request.toCredentials()
        val tokenPair = authUseCase.signIn(credentials)
        return ResponseEntity.ok(AuthDto.AuthResponse.fromTokenPair(tokenPair))
    }

    @PostMapping("/refresh")
    override fun refreshToken(@RequestBody request: AuthDto.TokenRefreshRequest): ResponseEntity<AuthDto.AuthResponse> {
        val tokenPair = authUseCase.refreshToken(request.refreshToken)
        return ResponseEntity.ok(AuthDto.AuthResponse.fromTokenPair(tokenPair))
    }

    @PostMapping("/signout")
    override fun signOut(@RequestHeader("Authorization") authorization: String): ResponseEntity<Void> {
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
