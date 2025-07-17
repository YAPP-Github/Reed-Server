package org.yapp.apis.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.yapp.apis.auth.dto.request.SocialLoginRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.AuthResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Authentication", description = "Authentication API")
interface AuthControllerApi {

    @Operation(
        summary = "Sign in or sign up with social login",
        description = "Sign in a user with social login credentials (Kakao or Apple). If the user doesn't exist, they will be automatically registered."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful sign in or sign up",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request or credentials",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Email already in use with a different account",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/signin")
    fun signIn(@RequestBody @Valid request: SocialLoginRequest): ResponseEntity<AuthResponse>

    @Operation(
        summary = "Refresh token",
        description = "Refresh an access token using a refresh token. Returns both a new access token and a new refresh token."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful token refresh",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid refresh token",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Refresh token not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody @Valid request: TokenRefreshRequest): ResponseEntity<AuthResponse>

    @Operation(summary = "Sign out", description = "Sign out a user by invalidating their refresh token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successful sign out"),
            ApiResponse(
                responseCode = "400",
                description = "Invalid user ID",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/signout")
    fun signOut(@AuthenticationPrincipal userId: UUID): ResponseEntity<Unit>

    @Operation(summary = "Get user profile", description = "Retrieves profile information for the given user ID.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User profile retrieved successfully",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/me")
    fun getUserProfile(@AuthenticationPrincipal userId: UUID): ResponseEntity<UserProfileResponse>
}
