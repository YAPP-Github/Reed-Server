package org.yapp.apis.readingrecord.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.domain.readingrecord.ReadingRecordDomainService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.domain.user.UserDomainService
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
class ReadingRecordService(
    private val readingRecordDomainService: ReadingRecordDomainService,
    private val userDomainService: UserDomainService
) {
    @Transactional
    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequest
    ): ReadingRecordResponse {
        val readingRecordInfoVO = readingRecordDomainService.createReadingRecord(
            userBookId = userBookId,
            pageNumber = request.validPageNumber(),
            quote = request.validQuote(),
            review = request.review,
            emotionTags = request.emotionTags
        )

        // Update user's lastActivity when a reading record is created
        userDomainService.updateLastActivity(userId)

        return ReadingRecordResponse.from(readingRecordInfoVO)
    }

    @Transactional(readOnly = true)
    fun getReadingRecordDetail(
        userId: UUID,
        readingRecordId: UUID
    ): ReadingRecordResponse {
        val readingRecordInfoVO = readingRecordDomainService.findReadingRecordById(readingRecordId)
        return ReadingRecordResponse.from(readingRecordInfoVO)
    }

    @Transactional(readOnly = true)
    fun getReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecordResponse> {
        val page = readingRecordDomainService.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        return page.map { ReadingRecordResponse.from(it) }
    }

    @Transactional
    fun deleteAllByUserBookId(userBookId: UUID) {
        readingRecordDomainService.deleteAllByUserBookId(userBookId)
    }

    @Transactional
    fun updateReadingRecord(
        userId: UUID,
        readingRecordId: UUID,
        request: UpdateReadingRecordRequest
    ): ReadingRecordResponse {
        val readingRecordInfoVO = readingRecordDomainService.modifyReadingRecord(
            readingRecordId = readingRecordId,
            pageNumber = request.pageNumber,
            quote = request.quote,
            review = request.review,
            emotionTags = request.emotionTags
        )

        // Update user's lastActivity when a reading record is updated
        userDomainService.updateLastActivity(userId)

        return ReadingRecordResponse.from(readingRecordInfoVO)
    }

    @Transactional
    fun deleteReadingRecord(
        readingRecordId: UUID
    ) {
        readingRecordDomainService.deleteReadingRecord(readingRecordId)
    }

    @Transactional(readOnly = true)
    fun getUserBookIdByReadingRecordId(readingRecordId: UUID): UUID {
        return readingRecordDomainService.findById(readingRecordId).userBookId.value
    }
}
