package org.yapp.apis.readingrecord.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.domain.readingrecord.ReadingRecordDomainService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
class ReadingRecordService(
    private val readingRecordDomainService: ReadingRecordDomainService,
) {
    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequest
    ): ReadingRecordResponse {
        val readingRecordInfoVO = readingRecordDomainService.createReadingRecord(
            userBookId = userBookId,
            pageNumber = request.validPageNumber(),
            quote = request.validQuote(),
            review = request.validReview(),
            emotionTags = request.validEmotionTags()
        )

        return ReadingRecordResponse.from(readingRecordInfoVO)
    }

    fun getReadingRecordDetail(
        userId: UUID,
        readingRecordId: UUID
    ): ReadingRecordResponse {
        val readingRecordInfoVO = readingRecordDomainService.findReadingRecordById(readingRecordId)
        return ReadingRecordResponse.from(readingRecordInfoVO)
    }

    fun getReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecordResponse> {
        val page = readingRecordDomainService.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        return page.map { ReadingRecordResponse.from(it) }
    }

    fun updateReadingRecord(
        readingRecordId: UUID,
        request: UpdateReadingRecordRequest
    ): ReadingRecordResponse {
        val readingRecordInfoVO = readingRecordDomainService.modifyReadingRecord(
            readingRecordId = readingRecordId,
            pageNumber = request.validPageNumber(),
            quote = request.validQuote(),
            review = request.validReview(),
            emotionTags = request.validEmotionTags()
        )
        return ReadingRecordResponse.from(readingRecordInfoVO)
    }

    fun deleteReadingRecord(
        readingRecordId: UUID
    ) {
        readingRecordDomainService.deleteReadingRecord(readingRecordId)
    }
}
