package org.yapp.apis.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yapp.apis.auth.dto.request.SocialLoginRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.AuthResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse

/**
 * API interface for authentication controller.
 */
@Tag(name = "Authentication", description = "Authentication API")
@RequestMapping("/api/v1/auth")
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
                description = "Invalid request or credentials"
            ),
            ApiResponse(
                responseCode = "409",
                description = "Email already in use with a different account"
            )
        ]
    )
    @PostMapping("/signin")
    fun signIn(@RequestBody @Valid request: SocialLoginRequest): ResponseEntity<AuthResponse>

    @Operation(
        summary = "Refresh token",
        description = "Refresh an access token using a refresh token"
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
                description = "Invalid refresh token"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Refresh token not found"
            )
        ]
    )
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody @Valid request: TokenRefreshRequest): ResponseEntity<AuthResponse>

    @Operation(
        summary = "Sign out",
        description = "Sign out a user by invalidating their refresh token"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Successful sign out"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid user ID"
            )
        ]
    )
    @PostMapping("/signout")
    fun signOut(@RequestHeader("Authorization") authorization: String): ResponseEntity<Unit>


    @Operation(
        summary = "Get user profile",
        description = "Retrieves profile information for the given user ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User profile retrieved successfully",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found"
            )
        ]
    )
    @GetMapping("/me")
    fun getUserProfile(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<UserProfileResponse>
}
