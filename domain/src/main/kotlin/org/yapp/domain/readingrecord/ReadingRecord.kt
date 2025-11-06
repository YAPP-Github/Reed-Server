package org.yapp.domain.readingrecord

import org.yapp.domain.userbook.UserBook
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

data class ReadingRecord private constructor(
    val id: Id,
    val userBookId: UserBook.Id,
    val pageNumber: PageNumber,
    val quote: Quote,
    val review: Review?,
    val emotionTags: List<EmotionTag> = emptyList(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(
            userBookId: UUID,
            pageNumber: Int,
            quote: String,
            review: String?,
            emotionTags: List<String> = emptyList()
        ): ReadingRecord {
            return ReadingRecord(
                id = Id.newInstance(UuidGenerator.create()),
                userBookId = UserBook.Id.newInstance(userBookId),
                pageNumber = PageNumber.newInstance(pageNumber),
                quote = Quote.newInstance(quote),
                review = Review.newInstance(review),
                emotionTags = emotionTags.map { EmotionTag.newInstance(it) }
            )
        }

        fun reconstruct(
            id: Id,
            userBookId: UserBook.Id,
            pageNumber: PageNumber,
            quote: Quote,
            review: Review?,
            emotionTags: List<EmotionTag> = emptyList(),
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null
        ): ReadingRecord {
            return ReadingRecord(
                id = id,
                userBookId = userBookId,
                pageNumber = pageNumber,
                quote = quote,
                review = review,
                emotionTags = emotionTags,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }
    }

    fun update(
        pageNumber: Int?,
        quote: String?,
        review: String?,
        emotionTags: List<String>?
    ): ReadingRecord {
        return this.copy(
            pageNumber = pageNumber?.let { PageNumber.newInstance(it) } ?: this.pageNumber,
            quote = quote?.let { Quote.newInstance(it) } ?: this.quote,
            review = if (review != null) Review.newInstance(review) else this.review,
            emotionTags = emotionTags?.map { EmotionTag.newInstance(it) } ?: this.emotionTags,
            updatedAt = LocalDateTime.now()
        )
    }

    @JvmInline
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }

    @JvmInline
    value class PageNumber(val value: Int) {
        companion object {
            fun newInstance(value: Int): PageNumber {
                require(value in 1..9999) { "Page number must be between 1 and 9999" }
                return PageNumber(value)
            }
        }
    }

    @JvmInline
    value class Quote(val value: String) {
        companion object {
            fun newInstance(value: String): Quote {
                require(value.isNotBlank()) { "Quote cannot be blank" }
                require(value.length <= 1000) { "Quote cannot exceed 1000 characters" }
                return Quote(value)
            }
        }
    }

    @JvmInline
    value class Review(val value: String) {
        companion object {
            fun newInstance(value: String?): Review? {
                if (value.isNullOrBlank()) {
                    return null
                }
                require(value.length <= 1000) { "Review cannot exceed 1000 characters" }
                return Review(value)
            }
        }
    }

    @JvmInline
    value class EmotionTag(val value: String) {
        companion object {
            fun newInstance(value: String): EmotionTag {
                require(value.isNotBlank()) { "Emotion tag cannot be blank" }
                require(value.length <= 10) { "Emotion tag cannot exceed 10 characters" }
                return EmotionTag(value)
            }
        }
    }

    fun delete(): ReadingRecord {
        return this.copy(deletedAt = LocalDateTime.now())
    }
}
