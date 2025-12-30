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
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponseV2
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*

@Tag(name = "Reading Records V2", description = "독서 기록 관련 API (V2)")
@RequestMapping("/api/v2/reading-records")
interface ReadingRecordControllerApiV2 {

    @Operation(
        summary = "독서 기록 생성 (V2)",
        description = "사용자의 책에 대한 독서 기록을 생성합니다. 대분류 감정은 필수, 세부 감정은 선택입니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "독서 기록 생성 성공",
                content = [Content(schema = Schema(implementation = ReadingRecordResponseV2::class))]
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
        @Valid @RequestBody @Parameter(description = "독서 기록 생성 요청 객체") request: CreateReadingRecordRequestV2
    ): ResponseEntity<ReadingRecordResponseV2>

    @Operation(
        summary = "독서 기록 상세 조회 (V2)",
        description = "독서 기록 ID로 독서 기록 상세 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "독서 기록 상세 조회 성공",
                content = [Content(schema = Schema(implementation = ReadingRecordResponseV2::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "해당 독서 기록에 대한 접근 권한이 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
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
    ): ResponseEntity<ReadingRecordResponseV2>

    @Operation(
        summary = "독서 기록 목록 조회 (V2)",
        description = "사용자의 책에 대한 독서 기록을 페이징하여 조회합니다. sort 파라미터가 지정된 경우 해당 정렬이 우선 적용되며, 지정하지 않으면 기본 정렬(updatedAt DESC)이 적용됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "독서 기록 목록 조회 성공"
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
        @RequestParam(required = false) @Parameter(
            description = "정렬 타입 (PAGE_NUMBER_ASC, PAGE_NUMBER_DESC, CREATED_DATE_ASC, CREATED_DATE_DESC, UPDATED_DATE_ASC, UPDATED_DATE_DESC). 지정 시 Pageable의 sort보다 우선 적용됨"
        ) sort: ReadingRecordSortType?,
        @PageableDefault(size = 10, sort = ["updatedAt"], direction = Sort.Direction.DESC)
        @Parameter(description = "페이지네이션 정보 (기본값: 10개). 정렬은 sort 파라미터로 제어되며, Pageable의 sort는 무시됩니다.") pageable: Pageable
    ): ResponseEntity<Page<ReadingRecordResponseV2>>

    @Operation(
        summary = "독서 기록 수정 (V2)",
        description = "독서 기록을 수정합니다. 대분류 감정과 세부 감정을 변경할 수 있습니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "독서 기록 수정 성공",
                content = [Content(schema = Schema(implementation = ReadingRecordResponseV2::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "해당 독서 기록에 대한 접근 권한이 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "독서 기록을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/{readingRecordId}")
    fun updateReadingRecord(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "수정할 독서 기록 ID") readingRecordId: UUID,
        @Valid @RequestBody @Parameter(description = "독서 기록 수정 요청 객체") request: UpdateReadingRecordRequestV2
    ): ResponseEntity<ReadingRecordResponseV2>

    @Operation(
        summary = "독서 기록 삭제",
        description = "독서 기록을 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "독서 기록 삭제 성공"),
            ApiResponse(
                responseCode = "403",
                description = "해당 독서 기록에 대한 접근 권한이 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "독서 기록을 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @DeleteMapping("/{readingRecordId}")
    fun deleteReadingRecord(
        @AuthenticationPrincipal @Parameter(description = "인증된 사용자 ID") userId: UUID,
        @PathVariable @Parameter(description = "삭제할 독서 기록 ID") readingRecordId: UUID
    ): ResponseEntity<Unit>
}
