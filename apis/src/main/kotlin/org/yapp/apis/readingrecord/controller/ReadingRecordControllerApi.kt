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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.globalutils.exception.ErrorResponse
import java.util.UUID

@Tag(name = "Reading Records", description = "독서 기록 관련 API")
@RequestMapping("/api/v1/user-books/{userBookId}/reading-records")
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
    @PostMapping
    fun createReadingRecord(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "독서 기록을 생성할 사용자 책 ID") userBookId: UUID,
        @Valid @RequestBody @Parameter(description = "독서 기록 생성 요청 객체") request: CreateReadingRecordRequest
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
                content = [Content(schema = Schema(implementation = ReadingRecordResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자 또는 책을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping
    fun getReadingRecords(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "독서 기록을 조회할 사용자 책 ID") userBookId: UUID,
        @RequestParam(required = false) @Parameter(description = "정렬 방식 (page_asc, page_desc, date_asc, date_desc)") sort: String?,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        @Parameter(description = "페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 방식)") pageable: Pageable
    ): ResponseEntity<Page<ReadingRecordResponse>>
}
