package org.yapp.infra.readingrecord.repository.impl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
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

        val results = queryFactory
            .selectFrom(readingRecord)
            .where(whereCondition)
            .orderBy(createOrderSpecifier(sort))
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

    private fun createOrderSpecifier(sort: ReadingRecordSortType?): OrderSpecifier<*> {
        return when (sort) {
            ReadingRecordSortType.PAGE_NUMBER_ASC -> readingRecord.pageNumber.asc()
            ReadingRecordSortType.PAGE_NUMBER_DESC -> readingRecord.pageNumber.desc()
            ReadingRecordSortType.CREATED_DATE_ASC -> readingRecord.createdAt.asc()
            ReadingRecordSortType.CREATED_DATE_DESC -> readingRecord.createdAt.desc()
            null -> readingRecord.createdAt.desc()
        }
    }

}
