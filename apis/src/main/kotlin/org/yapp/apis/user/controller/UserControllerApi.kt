package org.yapp.apis.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.yapp.apis.user.dto.request.FcmTokenRequest
import org.yapp.apis.user.dto.request.NotificationSettingsRequest
import org.yapp.apis.user.dto.request.TermsAgreementRequest
import org.yapp.apis.user.dto.response.UserProfileResponse
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Users", description = "사용자 정보를 관리하는 API")
@RequestMapping("/api/v1/users")
interface UserControllerApi {

    @Operation(
        summary = "사용자 프로필 조회",
        description = "현재 인증된 사용자의 프로필 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 프로필 조회 성공",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 사용자",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/me")
    fun getUserProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<UserProfileResponse>

    @Operation(
        summary = "약관 동의 상태 업데이트",
        description = "사용자의 약관 동의 상태를 업데이트합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "약관 동의 상태 업데이트 성공",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 사용자",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/me/terms-agreement")
    fun updateTermsAgreement(
        @Parameter(hidden = true) @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody @Parameter(description = "약관 동의 요청 객체") request: TermsAgreementRequest
    ): ResponseEntity<UserProfileResponse>

    @Operation(
        summary = "알림 설정 업데이트",
        description = "사용자의 알림 설정을 업데이트합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "알림 설정 업데이트 성공",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 사용자",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/me/notification-settings")
    fun updateNotificationSettings(
        @Parameter(hidden = true) @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody @Parameter(description = "알림 설정 요청 객체") request: NotificationSettingsRequest
    ): ResponseEntity<UserProfileResponse>

    @Operation(
        summary = "FCM 토큰 등록",
        description = "사용자의 FCM 토큰을 등록합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "FCM 토큰 등록 성공",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 사용자",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/me/fcm-token")
    fun updateFcmToken(
        @Parameter(hidden = true) @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody @Parameter(description = "FCM 토큰 요청 객체") request: FcmTokenRequest
    ): ResponseEntity<UserProfileResponse>
}
