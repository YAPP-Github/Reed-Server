package org.yapp.apis.readingrecord.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordPageResponse // Added import
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.usecase.ReadingRecordUseCase
import org.yapp.domain.readingrecord.ReadingRecordSortType
import java.util.UUID
import jakarta.validation.Valid
import org.yapp.apis.readingrecord.dto.response.SeedStatsResponse

@RestController
@RequestMapping("/api/v1/reading-records")
class ReadingRecordController(
    private val readingRecordUseCase: ReadingRecordUseCase
) : ReadingRecordControllerApi {

    @PostMapping("/{userBookId}")
    override fun createReadingRecord(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable userBookId: UUID,
        @Valid @RequestBody request: CreateReadingRecordRequest
    ): ResponseEntity<ReadingRecordResponse> {
        val response = readingRecordUseCase.createReadingRecord(
            userId = userId,
            userBookId = userBookId,
            request = request
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/detail/{readingRecordId}")
    override fun getReadingRecordDetail(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable readingRecordId: UUID
    ): ResponseEntity<ReadingRecordResponse> {
        val response = readingRecordUseCase.getReadingRecordDetail(
            userId = userId,
            readingRecordId = readingRecordId
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{userBookId}")
    override fun getReadingRecords(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable userBookId: UUID,
        @RequestParam(required = false) sort: ReadingRecordSortType?,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<ReadingRecordPageResponse> {
        val response = readingRecordUseCase.getReadingRecordsByUserBookId(
            userId = userId,
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{userBookId}/seed/stats")
    override fun getReadingRecordSeedStats(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable userBookId: UUID
    ): ResponseEntity<SeedStatsResponse> {
        val stats = readingRecordUseCase.getSeedStats(userId, userBookId)
        return ResponseEntity.ok(stats)
    }
}
