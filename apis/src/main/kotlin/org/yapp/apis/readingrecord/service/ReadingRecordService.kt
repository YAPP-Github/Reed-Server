package org.yapp.apis.readingrecord.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.exception.ReadingRecordErrorCode
import org.yapp.apis.readingrecord.exception.ReadingRecordNotFoundException
import org.yapp.domain.book.BookDomainService
import org.yapp.domain.readingrecord.ReadingRecordDomainService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import java.util.UUID


@Service
class ReadingRecordService(
    private val readingRecordDomainService: ReadingRecordDomainService,
    private val userBookService: UserBookService,
    private val bookDomainService: BookDomainService
) {

    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequest
    ): ReadingRecordResponse {
        userBookService.validateUserBookExists(userId, userBookId)


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
            ?: throw ReadingRecordNotFoundException(
                ReadingRecordErrorCode.READING_RECORD_NOT_FOUND,
                "Reading record not found with id: $readingRecordId"
            )

        userBookService.validateUserBookExists(userId, readingRecordInfoVO.userBookId.value)

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
}
