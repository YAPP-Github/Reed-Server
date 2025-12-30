package org.yapp.apis.readingrecord.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponseV2
import org.yapp.apis.readingrecord.usecase.ReadingRecordUseCaseV2
import org.yapp.domain.readingrecord.ReadingRecordSortType
import java.util.UUID

@RestController
@RequestMapping("/api/v2/reading-records")
class ReadingRecordControllerV2(
    private val readingRecordUseCaseV2: ReadingRecordUseCaseV2
) : ReadingRecordControllerApiV2 {

    @PostMapping("/{userBookId}")
    override fun createReadingRecord(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable userBookId: UUID,
        @Valid @RequestBody request: CreateReadingRecordRequestV2
    ): ResponseEntity<ReadingRecordResponseV2> {
        val response = readingRecordUseCaseV2.createReadingRecord(
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
    ): ResponseEntity<ReadingRecordResponseV2> {
        val response = readingRecordUseCaseV2.getReadingRecordDetail(
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
        @PageableDefault(size = 10, sort = ["updatedAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<Page<ReadingRecordResponseV2>> {
        val response = readingRecordUseCaseV2.getReadingRecordsByUserBookId(
            userId = userId,
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{readingRecordId}")
    override fun updateReadingRecord(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable readingRecordId: UUID,
        @Valid @RequestBody request: UpdateReadingRecordRequestV2
    ): ResponseEntity<ReadingRecordResponseV2> {
        val response = readingRecordUseCaseV2.updateReadingRecord(
            userId = userId,
            readingRecordId = readingRecordId,
            request = request
        )
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{readingRecordId}")
    override fun deleteReadingRecord(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable readingRecordId: UUID
    ): ResponseEntity<Unit> {
        readingRecordUseCaseV2.deleteReadingRecord(userId, readingRecordId)
        return ResponseEntity.noContent().build()
    }
}
