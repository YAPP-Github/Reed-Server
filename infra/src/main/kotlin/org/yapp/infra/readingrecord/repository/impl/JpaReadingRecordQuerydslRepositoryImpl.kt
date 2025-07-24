package org.yapp.infra.readingrecord.repository.impl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
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
        sort: String?,
        pageable: Pageable
    ): Page<ReadingRecordEntity> {
        val baseQuery = queryFactory
            .selectFrom(readingRecord)
            .where(
                readingRecord.userBookId.eq(userBookId)
            )

        val results = baseQuery
            .orderBy(createOrderSpecifier(sort))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(readingRecord.count())
            .from(readingRecord)
            .where(
                readingRecord.userBookId.eq(userBookId)
            )
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    private fun createOrderSpecifier(sort: String?): OrderSpecifier<*> {
        return when (sort) {
            "page_asc" -> readingRecord.pageNumber.asc()
            "page_desc" -> readingRecord.pageNumber.desc()
            "date_asc" -> readingRecord.createdAt.asc()
            "date_desc" -> readingRecord.createdAt.desc()
            else -> readingRecord.createdAt.desc()
        }
    }
}
