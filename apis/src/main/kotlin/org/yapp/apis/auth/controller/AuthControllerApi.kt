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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.yapp.apis.auth.dto.request.SocialLoginRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.request.WithdrawRequest
import org.yapp.apis.auth.dto.response.AuthResponse
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Authentication", description = "인증 관련 API")
interface AuthControllerApi {

    @Operation(
        summary = "소셜 로그인",
        description = "카카오 또는 애플 계정으로 로그인합니다. 사용자가 존재하지 않으면 자동으로 회원가입됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "로그인/회원가입 성공",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 또는 인증 정보",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 다른 계정으로 사용 중인 이메일",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/signin")
    fun signIn(@RequestBody @Valid request: SocialLoginRequest): ResponseEntity<AuthResponse>

    @Operation(
        summary = "토큰 갱신",
        description = "리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다. 새로운 액세스 토큰과 리프레시 토큰을 반환합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "토큰 갱신 성공",
                content = [Content(schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "유효하지 않은 리프레시 토큰",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "리프레시 토큰을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody @Valid request: TokenRefreshRequest): ResponseEntity<AuthResponse>

    @Operation(summary = "로그아웃", description = "리프레시 토큰을 무효화하여 사용자를 로그아웃합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "로그아웃 성공"),
            ApiResponse(
                responseCode = "400",
                description = "유효하지 않은 사용자 ID",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/signout")
    fun signOut(@AuthenticationPrincipal userId: UUID): ResponseEntity<Unit>

    @Operation(
        summary = "회원 탈퇴",
        description = "사용자 계정을 탈퇴합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 또는 사용자를 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Apple or Kakao 서버 연결 해제 실패",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @DeleteMapping("/withdraw")
    fun withdraw(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<Unit>
}
