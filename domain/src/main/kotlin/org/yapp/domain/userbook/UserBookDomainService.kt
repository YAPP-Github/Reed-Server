package org.yapp.domain.userbook

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.userbook.vo.HomeBookVO
import org.yapp.domain.userbook.vo.UserBookInfoVO
import org.yapp.domain.userbook.vo.UserBookStatusCountsVO
import org.yapp.globalutils.annotation.DomainService
import java.util.*

@DomainService
class UserBookDomainService(
    private val userBookRepository: UserBookRepository
) {
    fun upsertUserBook(
        userId: UUID,
        bookId: UUID,
        bookIsbn13: String,
        bookTitle: String,
        bookAuthor: String,
        bookPublisher: String,
        bookCoverImageUrl: String,
        status: BookStatus
    ): UserBookInfoVO {
        val userBook = userBookRepository.findByUserIdAndBookIsbn13(userId, bookIsbn13)?.updateStatus(status)
            ?: UserBook.create(
                userId = userId,
                bookId = bookId,
                bookIsbn13 = bookIsbn13,
                title = bookTitle,
                author = bookAuthor,
                publisher = bookPublisher,
                coverImageUrl = bookCoverImageUrl,
                status = status
            )

        val savedUserBook = userBookRepository.save(userBook)
        return UserBookInfoVO.newInstance(savedUserBook, savedUserBook.readingRecordCount)
    }

    fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): Page<UserBookInfoVO> {
        val page = userBookRepository.findUserBooksByDynamicCondition(userId, status, sort, title, pageable)
        return page.map { UserBookInfoVO.newInstance(it, it.readingRecordCount) }
    }

    fun findAllByUserIdAndBookIsbn13In(userId: UUID, isbn13s: List<String>): List<UserBookInfoVO> {
        if (isbn13s.isEmpty()) {
            return emptyList()
        }
        val userBooks = userBookRepository.findAllByUserIdAndBookIsbn13In(userId, isbn13s)
        return userBooks.map { UserBookInfoVO.newInstance(it, it.readingRecordCount) }
    }

    fun findByUserIdAndBookIsbn13(userId: UUID, isbn13: String): UserBookInfoVO? {
        val userBook = userBookRepository.findByUserIdAndBookIsbn13(userId, isbn13)
        return userBook?.let { UserBookInfoVO.newInstance(it, it.readingRecordCount) }
    }

    fun getUserBookStatusCounts(userId: UUID): UserBookStatusCountsVO {
        val statusCounts = BookStatus.entries.associateWith { status ->
            countUserBooksByStatus(userId, status)
        }
        return UserBookStatusCountsVO.newInstance(statusCounts)
    }

    private fun countUserBooksByStatus(userId: UUID, status: BookStatus): Long {
        return userBookRepository.countUserBooksByStatus(userId, status)
    }

    fun existsByUserBookIdAndUserId(userBookId: UUID, userId: UUID): Boolean {
        return userBookRepository.existsByIdAndUserId(userBookId, userId)
    }

    fun findBooksWithRecordsOrderByLatest(userId: UUID): List<HomeBookVO> {
        val resultTriples = userBookRepository.findRecordedBooksSortedByRecency(userId)

        return resultTriples.map { (userBook, lastRecordedAt, recordCount) ->
            HomeBookVO.newInstance(
                userBook = userBook,
                lastRecordedAt = lastRecordedAt,
                recordCount = recordCount.toInt()
            )
        }
    }

    fun findBooksWithoutRecordsByStatusPriority(
        userId: UUID,
        limit: Int,
        excludeIds: Set<UUID>
    ): List<HomeBookVO> {
        val userBooks = userBookRepository.findUnrecordedBooksSortedByPriority(
            userId,
            limit,
            excludeIds
        )

        return userBooks.map { userBook ->
            HomeBookVO.newInstance(
                userBook = userBook,
                lastRecordedAt = userBook.updatedAt ?: throw IllegalStateException("UserBook의 updatedAt이 null입니다: ${userBook.id}"),
                recordCount = 0
            )
        }
    }
}
