package org.yapp.apis.readingrecord.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordPageResponse
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.dto.response.SeedStatsResponse
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Reading Records", description = "독서 기록 관련 API")
@RequestMapping("/api/v1/reading-records")
interface ReadingRecordControllerApi {

    @Operation(
        summary = "독서 기록 생성",
        description = "사용자의 책에 대한 독서 기록을 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "독서 기록 생성 성공",
                content = [Content(schema = Schema(implementation = ReadingRecordResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자 또는 책을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/{userBookId}")
    fun createReadingRecord(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "독서 기록을 생성할 사용자 책 ID") userBookId: UUID,
        @Valid @RequestBody @Parameter(description = "독서 기록 생성 요청 객체") request: CreateReadingRecordRequest
    ): ResponseEntity<ReadingRecordResponse>

    @Operation(
        summary = "독서 기록 상세 조회",
        description = "독서 기록 ID로 독서 기록 상세 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "독서 기록 상세 조회 성공",
                content = [Content(schema = Schema(implementation = ReadingRecordResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자 또는 독서 기록을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/detail/{readingRecordId}")
    fun getReadingRecordDetail(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "조회할 독서 기록 ID") readingRecordId: UUID
    ): ResponseEntity<ReadingRecordResponse>

    @Operation(
        summary = "독서 기록 목록 조회",
        description = "사용자의 책에 대한 독서 기록을 페이징하여 조회합니다. 정렬은 페이지 번호 또는 최신 등록순으로 가능합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "독서 기록 목록 조회 성공",
                content = [Content(schema = Schema(implementation = Page::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자 또는 책을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/{userBookId}")
    fun getReadingRecords(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "독서 기록을 조회할 사용자 책 ID") userBookId: UUID,
        @RequestParam(required = false) @Parameter(description = "정렬 방식 (PAGE_NUMBER_ASC, PAGE_NUMBER_DESC, CREATED_DATE_ASC, CREATED_DATE_DESC)") sort: ReadingRecordSortType?,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        @Parameter(description = "페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 방식)") pageable: Pageable
    ): ResponseEntity<ReadingRecordPageResponse>

    @Operation(
        summary = "씨앗 통계 조회",
        description = "사용자가 등록한 책에 기록된 씨앗 개수를 조회합니다."
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
    @GetMapping("/{userBookId}/seed/stats")
    fun getReadingRecordSeedStats(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable userBookId: UUID
    ): ResponseEntity<SeedStatsResponse>
}
