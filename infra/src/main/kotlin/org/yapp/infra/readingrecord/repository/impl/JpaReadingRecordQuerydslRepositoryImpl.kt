package org.yapp.infra.readingrecord.repository.impl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.infra.readingrecord.entity.QReadingRecordEntity
import org.yapp.infra.readingrecord.entity.ReadingRecordEntity
import org.yapp.infra.readingrecord.repository.JpaReadingRecordQuerydslRepository
import java.util.*

@Repository
class JpaReadingRecordQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : JpaReadingRecordQuerydslRepository {

    private val readingRecord = QReadingRecordEntity.readingRecordEntity

    override fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecordEntity> {

        val whereCondition = readingRecord.userBookId.eq(userBookId)
            .and(readingRecord.deletedAt.isNull())

        val results = queryFactory
            .selectFrom(readingRecord)
            .where(whereCondition)
            .orderBy(*createOrderSpecifiers(sort))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(readingRecord.count())
            .from(readingRecord)
            .where(whereCondition)
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    override fun findMostFrequentPrimaryEmotion(userBookId: UUID): PrimaryEmotion? {
        return queryFactory
            .select(readingRecord.primaryEmotion)
            .from(readingRecord)
            .where(
                readingRecord.userBookId.eq(userBookId)
                    .and(readingRecord.deletedAt.isNull())
            )
            .groupBy(readingRecord.primaryEmotion)
            .orderBy(
                readingRecord.id.count().desc(),
                readingRecord.createdAt.max().desc()
            )
            .fetchFirst()
    }

    override fun countPrimaryEmotionsByUserBookId(userBookId: UUID): Map<PrimaryEmotion, Int> {
        val results = queryFactory
            .select(readingRecord.primaryEmotion, readingRecord.count())
            .from(readingRecord)
            .where(
                readingRecord.userBookId.eq(userBookId)
                    .and(readingRecord.deletedAt.isNull())
            )
            .groupBy(readingRecord.primaryEmotion)
            .fetch()

        return results.associate { tuple ->
            val emotion = tuple[readingRecord.primaryEmotion] ?: PrimaryEmotion.OTHER
            val count = tuple[readingRecord.count()]?.toInt() ?: 0
            emotion to count
        }
    }

    private fun createOrderSpecifiers(sort: ReadingRecordSortType?): Array<OrderSpecifier<*>> {
        return when (sort) {
            ReadingRecordSortType.PAGE_NUMBER_ASC -> arrayOf(
                readingRecord.pageNumber.asc(),
                readingRecord.updatedAt.desc()
            )

            ReadingRecordSortType.PAGE_NUMBER_DESC -> arrayOf(
                readingRecord.pageNumber.desc(),
                readingRecord.updatedAt.desc()
            )

            ReadingRecordSortType.CREATED_DATE_ASC -> arrayOf(readingRecord.createdAt.asc())
            ReadingRecordSortType.CREATED_DATE_DESC -> arrayOf(readingRecord.createdAt.desc())
            ReadingRecordSortType.UPDATED_DATE_ASC -> arrayOf(readingRecord.updatedAt.asc())
            ReadingRecordSortType.UPDATED_DATE_DESC -> arrayOf(readingRecord.updatedAt.desc())
            null -> arrayOf(readingRecord.updatedAt.desc())
        }
    }
}

