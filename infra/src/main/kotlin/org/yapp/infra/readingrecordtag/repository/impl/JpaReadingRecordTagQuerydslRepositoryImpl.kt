package org.yapp.infra.readingrecordtag.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.yapp.infra.readingrecord.entity.QReadingRecordEntity
import org.yapp.infra.readingrecordtag.entity.QReadingRecordTagEntity
import org.yapp.infra.readingrecordtag.repository.JpaReadingRecordTagQuerydslRepository
import org.yapp.infra.tag.entity.QTagEntity
import org.yapp.infra.userbook.entity.QUserBookEntity
import java.util.*

@Repository
class JpaReadingRecordTagQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : JpaReadingRecordTagQuerydslRepository {

    private val readingRecordTag = QReadingRecordTagEntity.readingRecordTagEntity
    private val readingRecord = QReadingRecordEntity.readingRecordEntity
    private val userBook = QUserBookEntity.userBookEntity
    private val tag = QTagEntity.tagEntity

    override fun countTagsByUserIdAndUserBookIdAndCategories(
        userId: UUID,
        userBookId: UUID,
        categories: List<String>
    ): Map<String, Int> {
        if (categories.isEmpty()) {
            return emptyMap()
        }

        val results = queryFactory
            .select(tag.name, readingRecordTag.count())
            .from(readingRecordTag)
            .join(readingRecord).on(readingRecordTag.readingRecordId.eq(readingRecord.id))
            .join(userBook).on(readingRecord.userBookId.eq(userBook.id))
            .join(tag).on(readingRecordTag.tagId.eq(tag.id))
            .where(
                userBook.userIdEq(userId),
                readingRecord.userBookIdEq(userBookId),
                readingRecord.isNotDeleted(),
                readingRecordTag.isNotDeleted(),
                tag.nameIn(categories)
            )
            .groupBy(tag.name)
            .fetch()

        return results.associate { tuple ->
            val tagName = tuple[tag.name] ?: ""
            val count = tuple[readingRecordTag.count()]?.toInt() ?: 0
            tagName to count
        }
    }

    private fun QUserBookEntity.userIdEq(userId: UUID): BooleanExpression {
        return this.userId.eq(userId)
    }

    private fun QReadingRecordEntity.userBookIdEq(userBookId: UUID): BooleanExpression {
        return this.userBookId.eq(userBookId)
    }

    private fun QReadingRecordEntity.isNotDeleted(): BooleanExpression {
        return this.deletedAt.isNull
    }

    private fun QReadingRecordTagEntity.isNotDeleted(): BooleanExpression {
        return this.deletedAt.isNull
    }

    private fun QTagEntity.nameIn(categories: List<String>): BooleanExpression {
        return this.name.`in`(categories)
    }
}
