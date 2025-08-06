package org.yapp.apis.auth.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.yapp.apis.auth.dto.request.SocialLoginRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.AuthResponse
import org.yapp.apis.auth.usecase.AuthUseCase
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authUseCase: AuthUseCase
) : AuthControllerApi {

    @PostMapping("/signin")
    override fun signIn(@RequestBody @Valid request: SocialLoginRequest): ResponseEntity<AuthResponse> {
        val tokenPair = authUseCase.signIn(request)
        return ResponseEntity.ok(AuthResponse.fromTokenPair(tokenPair))
    }

    @PostMapping("/refresh")
    override fun refreshToken(@RequestBody @Valid request: TokenRefreshRequest): ResponseEntity<AuthResponse> {
        val tokenPair = authUseCase.reissueTokenPair(request)
        return ResponseEntity.ok(AuthResponse.fromTokenPair(tokenPair))
    }

    @PostMapping("/signout")
    override fun signOut(@AuthenticationPrincipal userId: UUID): ResponseEntity<Unit> {
        authUseCase.signOut(userId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/withdraw")
    override fun withdraw(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<Unit> {
        authUseCase.withdraw(userId)
        return ResponseEntity.noContent().build()
    }
}
