package org.yapp.apis.book.service

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.apis.book.dto.request.UpsertUserBookRequest
import org.yapp.apis.book.dto.request.UserBooksByIsbn13sRequest
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.exception.UserBookErrorCode
import org.yapp.apis.book.exception.UserBookException
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
class UserBookService(
    private val userBookDomainService: UserBookDomainService
) {
    fun upsertUserBook(@Valid upsertUserBookRequest: UpsertUserBookRequest): UserBookResponse {
        val userBookInfoVO = userBookDomainService.upsertUserBook(
            upsertUserBookRequest.validUserId(),
            upsertUserBookRequest.validBookId(),
            upsertUserBookRequest.validBookIsbn13(),
            upsertUserBookRequest.validBookTitle(),
            upsertUserBookRequest.validBookAuthor(),
            upsertUserBookRequest.validBookPublisher(),
            upsertUserBookRequest.validBookCoverImageUrl(),
            upsertUserBookRequest.validStatus()
        )
        return UserBookResponse.from(userBookInfoVO)
    }

    fun validateUserBookExists(userBookId: UUID, userId: UUID) {
        if (!userBookDomainService.existsByUserBookIdAndUserId(userBookId, userId)) {
            throw UserBookException(
                UserBookErrorCode.USER_BOOK_NOT_FOUND,
                "UserBook not found or access denied: $userBookId"
            )
        }
    }

    fun findAllByUserIdAndBookIsbn13In(@Valid userBooksByIsbn13sRequest: UserBooksByIsbn13sRequest): List<UserBookResponse> {
        val userBooks = userBookDomainService.findAllByUserIdAndBookIsbn13In(
            userBooksByIsbn13sRequest.validUserId(),
            userBooksByIsbn13sRequest.validIsbn13s(),
        )
        return userBooks.map { UserBookResponse.from(it) }
    }

    fun findUserBookStatusByIsbn13(userId: UUID, isbn13: String): BookStatus? {
        val userBook = userBookDomainService.findByUserIdAndBookIsbn13(userId, isbn13)
        return userBook?.status
    }

    private fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): Page<UserBookResponse> {
        val page = userBookDomainService.findUserBooksByDynamicCondition(userId, status, sort, title, pageable)
        return page.map { UserBookResponse.from(it) }
    }

    fun findUserBooksByDynamicConditionWithStatusCounts(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): UserBookPageResponse {
        val userBookResponsePage = findUserBooksByDynamicCondition(userId, status, sort, title, pageable)
        val userBookStatusCountsVO = userBookDomainService.getUserBookStatusCounts(userId)

        return UserBookPageResponse.of(
            books = userBookResponsePage,
            beforeReadingCount = userBookStatusCountsVO.beforeReadingCount,
            readingCount = userBookStatusCountsVO.readingCount,
            completedCount = userBookStatusCountsVO.completedCount
        )
    }

    fun deleteUserBook(userBookId: UUID, userId: UUID) {
        validateUserBookExists(userBookId, userId)
        userBookDomainService.deleteById(userBookId)
    }
}
