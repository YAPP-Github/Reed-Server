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
        bookIsbn: String,
        bookTitle: String,
        bookAuthor: String,
        bookPublisher: String,
        bookCoverImageUrl: String,
        status: BookStatus
    ): UserBookInfoVO {
        val userBook = userBookRepository.findByUserIdAndBookIsbn(userId, bookIsbn)?.updateStatus(status)
            ?: UserBook.create(
                userId = userId,
                bookId = bookId,
                bookIsbn = bookIsbn,
                title = bookTitle,
                author = bookAuthor,
                publisher = bookPublisher,
                coverImageUrl = bookCoverImageUrl,
                status = status
            )

        val savedUserBook = userBookRepository.save(userBook)
        return UserBookInfoVO.newInstance(savedUserBook)
    }

    fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        pageable: Pageable
    ): Page<UserBookInfoVO> {
        val page = userBookRepository.findUserBooksByDynamicCondition(userId, status, sort, pageable)
        return page.map { UserBookInfoVO.newInstance(it) }
    }

    fun findAllByUserIdAndBookIsbnIn(userId: UUID, isbns: List<String>): List<UserBookInfoVO> {
        if (isbns.isEmpty()) {
            return emptyList()
        }
        val userBooks = userBookRepository.findAllByUserIdAndBookIsbnIn(userId, isbns)
        return userBooks.map { UserBookInfoVO.newInstance(it) }
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

    fun findByIdAndUserId(userBookId: UUID, userId: UUID): UserBook? {
        return userBookRepository.findByIdAndUserId(userBookId, userId)
    }

    fun findBooksWithRecordsOrderByLatest(userId: UUID): List<HomeBookVO> {
        val resultPairs = userBookRepository.findRecordedBooksSortedByRecency(userId)

        return resultPairs.map { (userBook, lastRecordedAt) ->
            HomeBookVO.newInstance(
                userBook = userBook,
                lastRecordedAt = lastRecordedAt
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
                lastRecordedAt = userBook.updatedAt!!
            )
        }
    }
}
