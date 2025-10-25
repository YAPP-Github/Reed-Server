package org.yapp.infra.readingrecord.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.*

@Entity
@Table(name = "reading_records")
@SQLDelete(sql = "UPDATE reading_records SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class ReadingRecordEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_book_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val userBookId: UUID,

    pageNumber: Int,
    quote: String,
    review: String?,

    
) : BaseTimeEntity() {

    @Column(name = "page_number", nullable = false)
    var pageNumber: Int = pageNumber
        protected set

    @Column(name = "quote", nullable = false, length = 1000)
    var quote: String = quote
        protected set

    @Column(name = "review", nullable = true, length = 1000)
    var review: String? = review
        protected set

    fun toDomain(): ReadingRecord {
        return ReadingRecord.reconstruct(
            id = ReadingRecord.Id.newInstance(this.id),
            userBookId = ReadingRecord.UserBookId.newInstance(this.userBookId),
            pageNumber = ReadingRecord.PageNumber.newInstance(this.pageNumber),
            quote = ReadingRecord.Quote.newInstance(this.quote),
            review = this.review?.let { ReadingRecord.Review.newInstance(it) },
            emotionTags = emptyList(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }

    companion object {
        fun fromDomain(readingRecord: ReadingRecord): ReadingRecordEntity {
            return ReadingRecordEntity(
                id = readingRecord.id.value,
                userBookId = readingRecord.userBookId.value,
                pageNumber = readingRecord.pageNumber.value,
                quote = readingRecord.quote.value,
                review = readingRecord.review?.value
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ReadingRecordEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
