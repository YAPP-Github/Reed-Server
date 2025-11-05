package org.yapp.domain.readingrecord.vo

import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.domain.userbook.UserBook
import java.time.LocalDateTime

data class ReadingRecordInfoVO private constructor(
    val id: ReadingRecord.Id,
    val userBookId: UserBook.Id,
    val pageNumber: ReadingRecord.PageNumber,
    val quote: ReadingRecord.Quote,
    val review: ReadingRecord.Review?,
    val emotionTags: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val bookTitle: String? = null,
    val bookPublisher: String? = null,
    val bookCoverImageUrl: String? = null,
    val author: String? = null
) {
    init {
        require(emotionTags.size <= 3) { "Maximum 3 emotion tags are allowed" }
        require(!createdAt.isAfter(updatedAt)) {
            "생성일(createdAt)은 수정일(updatedAt)보다 이후일 수 없습니다."
        }
    }

    companion object {
        fun newInstance(
            readingRecord: ReadingRecord,
            emotionTags: List<String>,
            bookTitle: String? = null,
            bookPublisher: String? = null,
            bookCoverImageUrl: String? = null,
            author: String? = null
        ): ReadingRecordInfoVO {
            return ReadingRecordInfoVO(
                id = readingRecord.id,
                userBookId = readingRecord.userBookId,
                pageNumber = readingRecord.pageNumber,
                quote = readingRecord.quote,
                review = readingRecord.review,
                emotionTags = emotionTags,
                createdAt = readingRecord.createdAt ?: throw IllegalStateException("createdAt은 null일 수 없습니다."),
                updatedAt = readingRecord.updatedAt ?: throw IllegalStateException("updatedAt은 null일 수 없습니다."),
                bookTitle = bookTitle,
                bookPublisher = bookPublisher,
                bookCoverImageUrl = bookCoverImageUrl,
                author = author
            )
        }
    }
}
