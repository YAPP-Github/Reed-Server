package org.yapp.infra.userbook.repository.impl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.infra.readingrecord.entity.QReadingRecordEntity
import org.yapp.infra.userbook.entity.QUserBookEntity
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.JpaUserBookQuerydslRepository
import org.yapp.infra.userbook.repository.dto.QUserBookLastRecordProjection
import org.yapp.infra.userbook.repository.dto.UserBookLastRecordProjection
import java.util.*

@Repository
class JpaUserBookQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : JpaUserBookQuerydslRepository {

    private val userBook = QUserBookEntity.userBookEntity
    private val readingRecord = QReadingRecordEntity.readingRecordEntity

    override fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): Page<UserBookEntity> {
        val baseQuery = queryFactory
            .selectFrom(userBook)
            .where(
                userBook.userId.eq(userId),
                statusEq(status),
                titleContains(title)
            )

        val results = baseQuery
            .orderBy(createOrderSpecifier(sort))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(userBook.count())
            .from(userBook)
            .where(
                userBook.userId.eq(userId),
                statusEq(status),
                titleContains(title)
            )
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

    override fun countUserBooksByStatus(
        userId: UUID,
        status: BookStatus
    ): Long {
        return queryFactory
            .select(userBook.count())
            .from(userBook)
            .where(
                userBook.userId.eq(userId),
                userBook.status.eq(status)
            )
            .fetchOne() ?: 0L
    }

    override fun findRecordedBooksSortedByRecency(userId: UUID): List<UserBookLastRecordProjection> {
        return queryFactory
            .select(
                QUserBookLastRecordProjection(
                    userBook,
                    readingRecord.updatedAt.max(),
                    readingRecord.count()
                )
            )
            .from(userBook)
            .join(readingRecord).on(readingRecord.userBookId.eq(userBook.id))
            .where(
                userBook.userIdEq(userId),
                userBook.isNotDeleted()
            )
            .groupBy(userBook.id)
            .orderBy(readingRecord.updatedAt.max().desc())
            .fetch()
    }

    override fun findUnrecordedBooksSortedByPriority(
        userId: UUID,
        excludeIds: Set<UUID>,
        limit: Int
    ): List<UserBookEntity> {
        return queryFactory
            .selectFrom(userBook)
            .where(
                userBook.userIdEq(userId),
                userBook.isNotDeleted(),
                userBook.hasNoRecords(),
                userBook.idNotIn(excludeIds)
            )
            .orderBy(
                statusPriorityOrder(),
                userBook.updatedAt.desc()
            )
            .limit(limit.toLong())
            .fetch()
    }

    private fun QUserBookEntity.userIdEq(userId: UUID): BooleanExpression {
        return this.userId.eq(userId)
    }

    private fun QUserBookEntity.isNotDeleted(): BooleanExpression {
        return this.deletedAt.isNull
    }

    private fun QUserBookEntity.hasNoRecords(): BooleanExpression {
        return JPAExpressions.selectOne()
            .from(readingRecord)
            .where(readingRecord.userBookId.eq(this.id))
            .notExists()
    }

    private fun QUserBookEntity.idNotIn(excludeIds: Set<UUID>): BooleanExpression? {
        return if (excludeIds.isNotEmpty()) {
            this.id.notIn(excludeIds)
        } else {
            null
        }
    }

    private fun statusPriorityOrder(): OrderSpecifier<*> {
        return CaseBuilder()
            .`when`(userBook.status.eq(BookStatus.READING)).then(1)
            .`when`(userBook.status.eq(BookStatus.COMPLETED)).then(2)
            .`when`(userBook.status.eq(BookStatus.BEFORE_READING)).then(3)
            .otherwise(4)
            .asc()
    }

    private fun statusEq(status: BookStatus?): BooleanExpression? {
        return status?.let { userBook.status.eq(it) }
    }

    private fun titleContains(title: String?): BooleanExpression? {
        return title?.takeIf { it.isNotBlank() }?.let {
            userBook.title.like("%" + it + "%")
        }
    }

    private fun createOrderSpecifier(sort: UserBookSortType?): OrderSpecifier<*> {
        return when (sort) {
            UserBookSortType.TITLE_ASC -> userBook.title.asc()
            UserBookSortType.TITLE_DESC -> userBook.title.desc()
            UserBookSortType.CREATED_DATE_ASC -> userBook.createdAt.asc()
            UserBookSortType.CREATED_DATE_DESC -> userBook.createdAt.desc()
            UserBookSortType.UPDATED_DATE_ASC -> userBook.updatedAt.asc()
            UserBookSortType.UPDATED_DATE_DESC -> userBook.updatedAt.desc()
            null -> userBook.updatedAt.desc()
        }
    }
}
