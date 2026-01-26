package org.yapp.apis.emotion.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.yapp.apis.emotion.dto.response.EmotionListResponse

@Tag(name = "Emotions", description = "감정 관련 API")
@RequestMapping("/api/v2/emotions")
interface EmotionControllerApi {

    @Operation(
        summary = "감정 목록 조회",
        description = "대분류 감정과 세부 감정 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "감정 목록 조회 성공",
                content = [Content(schema = Schema(implementation = EmotionListResponse::class))]
            )
        ]
    )
    @GetMapping
    fun getEmotions(): ResponseEntity<EmotionListResponse>
}
