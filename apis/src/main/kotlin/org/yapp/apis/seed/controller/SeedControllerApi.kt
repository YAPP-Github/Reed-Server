package org.yapp.apis.seed.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.yapp.apis.seed.dto.response.SeedStatsResponse
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Seed", description = "씨앗(감정 태그) 관련 API")
interface SeedControllerApi {

    @Operation(
        summary = "씨앗 통계 조회",
        description = "사용자가 모은 감정 태그별 씨앗 개수를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "씨앗 통계 조회 성공",
                content = [Content(schema = Schema(implementation = SeedStatsResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/stats")
    fun getSeedStats(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<SeedStatsResponse>
} 