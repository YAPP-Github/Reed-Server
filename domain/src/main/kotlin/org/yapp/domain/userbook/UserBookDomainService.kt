package org.yapp.domain.userbook

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.userbook.vo.UserBookInfoVO
import org.yapp.domain.userbook.vo.UserBookStatusCountsVO
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class UserBookDomainService(
    private val userBookRepository: UserBookRepository
) {
    fun upsertUserBook(
        userId: UUID,
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

    fun findAllUserBooks(userId: UUID): List<UserBookInfoVO> {
        return userBookRepository.findAllByUserId(userId)
            .map(UserBookInfoVO::newInstance)
    }

    fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: String?,
        pageable: Pageable
    ): Page<UserBookInfoVO> {
        return userBookRepository.findUserBooksByDynamicCondition(userId, status, sort, pageable)
            .map(UserBookInfoVO::newInstance)
    }

    fun findAllByUserIdAndBookIsbnIn(userId: UUID, isbns: List<String>): List<UserBookInfoVO> {
        if (isbns.isEmpty()) {
            return emptyList()
        }
        return userBookRepository.findAllByUserIdAndBookIsbnIn(userId, isbns)
            .map { UserBookInfoVO.newInstance(it) }
    }


    fun getUserBookStatusCounts(userId: UUID): UserBookStatusCountsVO {
        val statusCounts = mapOf(
            BookStatus.BEFORE_READING to countUserBooksByStatus(userId, BookStatus.BEFORE_READING),
            BookStatus.READING to countUserBooksByStatus(userId, BookStatus.READING),
            BookStatus.COMPLETED to countUserBooksByStatus(userId, BookStatus.COMPLETED)
        )
        return UserBookStatusCountsVO.newInstance(statusCounts)
    }


    private fun countUserBooksByStatus(userId: UUID, status: BookStatus): Long {
        return userBookRepository.countUserBooksByStatus(userId, status)
    }
}
