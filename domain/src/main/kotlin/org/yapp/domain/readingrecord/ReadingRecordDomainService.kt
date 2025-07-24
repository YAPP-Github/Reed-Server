package org.yapp.domain.readingrecord

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.readingrecord.vo.ReadingRecordInfoVO
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class ReadingRecordDomainService(
    private val readingRecordRepository: ReadingRecordRepository
) {

    fun createReadingRecord(
        userBookId: UUID,
        pageNumber: Int,
        quote: String,
        review: String,
        emotionTags: List<String>
    ): ReadingRecordInfoVO {

        val readingRecord = ReadingRecord.create(
            userBookId = userBookId,
            pageNumber = pageNumber,
            quote = quote,
            review = review,
            emotionTags = emotionTags
        )

        val savedReadingRecord = readingRecordRepository.save(readingRecord)
        return ReadingRecordInfoVO.newInstance(savedReadingRecord)
    }


    fun findAllReadingRecordsByUserBookId(userBookId: UUID): List<ReadingRecordInfoVO> {
        val readingRecords = readingRecordRepository.findAllByUserBookId(userBookId)
        return readingRecords.map { ReadingRecordInfoVO.newInstance(it) }
    }

    fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: String?,
        pageable: Pageable
    ): Page<ReadingRecordInfoVO> {
        val page = readingRecordRepository.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        return page.map { ReadingRecordInfoVO.newInstance(it) }
    }

}
