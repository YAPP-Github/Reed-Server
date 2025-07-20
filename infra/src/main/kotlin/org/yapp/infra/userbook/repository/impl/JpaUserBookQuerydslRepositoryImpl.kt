package org.yapp.infra.userbook.repository.impl

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.yapp.domain.userbook.BookStatus
import org.yapp.infra.userbook.entity.QUserBookEntity
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.JpaUserBookQuerydslRepository
import java.util.UUID

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
        val query = queryFactory
            .selectFrom(userBook)
            .where(
                userBook.userId.eq(userId),
                statusEq(status)
            )
            .orderBy(*createOrderSpecifier(sort))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
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

    private fun createOrderSpecifier(sort: String?): Array<OrderSpecifier<*>> {
        return when (sort) {
            "title_asc" -> arrayOf(OrderSpecifier(Order.ASC, userBook.title))
            "title_desc" -> arrayOf(OrderSpecifier(Order.DESC, userBook.title))
            "date_asc" -> arrayOf(OrderSpecifier(Order.ASC, userBook.createdAt))
            "date_desc" -> arrayOf(OrderSpecifier(Order.DESC, userBook.createdAt))
            else -> arrayOf(OrderSpecifier(Order.DESC, userBook.createdAt)) // 기본 정렬
        }
    }
}
