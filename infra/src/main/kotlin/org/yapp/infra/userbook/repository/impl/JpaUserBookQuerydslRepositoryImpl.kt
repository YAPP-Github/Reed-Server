package org.yapp.infra.userbook.repository.impl

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yapp.domain.userbook.BookStatus
import org.yapp.infra.userbook.entity.QUserBookEntity
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.JpaUserBookQuerydslRepository
import java.util.*

@Repository
class JpaUserBookQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : JpaUserBookQuerydslRepository {

    private val userBook = QUserBookEntity.userBookEntity

    override fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: String?,
        pageable: Pageable
    ): Page<UserBookEntity> {
        val baseQuery = queryFactory
            .selectFrom(userBook)
            .where(
                userBook.userId.eq(userId),
                statusEq(status)
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
                        statusEq(status)
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

    private fun statusEq(status: BookStatus?): BooleanExpression? {
        return status?.let { userBook.status.eq(it) }
    }

    private fun createOrderSpecifier(sort: String?): OrderSpecifier<*> {
        return when (sort) {
            "title_asc" -> userBook.title.asc()
            "title_desc" -> userBook.title.desc()
            "date_asc" -> userBook.createdAt.asc()
            "date_desc" -> userBook.createdAt.desc()
            else -> userBook.createdAt.desc()
        }
    }
}
