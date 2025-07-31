package org.yapp.apis.home.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.yapp.apis.home.dto.response.UserHomeResponse
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Home", description = "홈 화면 관련 API")
@RequestMapping("/api/home")
interface HomeControllerApi {

    @Operation(
        summary = "홈 화면 데이터 조회",
        description = "사용자의 홈 화면에 필요한 데이터를 조회합니다. 요즘 읽는 책 목록을 우선순위에 따라 최대 limit개까지 반환합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "홈 화면 데이터 조회 성공",
                content = [Content(schema = Schema(implementation = UserHomeResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping
    fun getUserHomeData(
        @AuthenticationPrincipal userId: UUID,
        @Parameter(
            description = "조회할 최대 도서 수 (기본값: 3, 최대: 10)",
            example = "3"
        )
        @RequestParam(defaultValue = "3") @Min(1) @Max(10) limit: Int
    ): ResponseEntity<UserHomeResponse>
} 
