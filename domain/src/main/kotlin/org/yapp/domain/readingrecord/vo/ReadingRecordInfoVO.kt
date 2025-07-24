package org.yapp.domain.readingrecord.vo

import org.yapp.domain.readingrecord.ReadingRecord
import java.time.LocalDateTime

data class ReadingRecordInfoVO private constructor(
    val id: ReadingRecord.Id,
    val userBookId: ReadingRecord.UserBookId,
    val pageNumber: ReadingRecord.PageNumber,
    val quote: ReadingRecord.Quote,
    val review: ReadingRecord.Review,
    val emotionTags: List<ReadingRecord.EmotionTag>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(emotionTags.size <= 3) { "Maximum 3 emotion tags are allowed" }
        require(createdAt.isBefore(updatedAt) || createdAt == updatedAt) {
            "생성일(createdAt)은 수정일(updatedAt)보다 이후일 수 없습니다."
        }
    }

    companion object {
        fun newInstance(
            readingRecord: ReadingRecord,
        ): ReadingRecordInfoVO {
            return ReadingRecordInfoVO(
                id = readingRecord.id,
                userBookId = readingRecord.userBookId,
                pageNumber = readingRecord.pageNumber,
                quote = readingRecord.quote,
                review = readingRecord.review,
                emotionTags = readingRecord.emotionTags,
                createdAt = readingRecord.createdAt ?: throw IllegalStateException("createdAt은 null일 수 없습니다."),
                updatedAt = readingRecord.updatedAt ?: throw IllegalStateException("updatedAt은 null일 수 없습니다.")
            )
        }
    }
}
