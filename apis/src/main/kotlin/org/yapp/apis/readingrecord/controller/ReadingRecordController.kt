package org.yapp.apis.readingrecord.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.usecase.ReadingRecordUseCase
import java.util.UUID
import jakarta.validation.Valid

@RestController
class ReadingRecordController(
    private val readingRecordUseCase: ReadingRecordUseCase
) : ReadingRecordControllerApi {

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

    override fun getReadingRecords(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable userBookId: UUID,
        @RequestParam(required = false) sort: String?,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<Page<ReadingRecordResponse>> {
        val response = readingRecordUseCase.getReadingRecordsByUserBookId(
            userId = userId,
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
        return ResponseEntity.ok(response)
    }
}
