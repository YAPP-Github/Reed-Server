package org.yapp.apis.readingrecord.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.domain.readingrecord.ReadingRecordDomainService
import java.util.UUID


@Service
class ReadingRecordService(
    private val readingRecordDomainService: ReadingRecordDomainService
) {

    fun createReadingRecord(
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


    fun getReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: String?,
        pageable: Pageable
    ): Page<ReadingRecordResponse> {
        val page = readingRecordDomainService.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        return page.map { ReadingRecordResponse.from(it) }
    }
}
