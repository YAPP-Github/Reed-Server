package org.yapp.apis.book.service

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.dto.request.UserBooksByIsbnsRequest
import org.yapp.apis.book.dto.request.UpsertUserBookRequest
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.exception.UserBookErrorCode
import org.yapp.apis.book.exception.UserBookException
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.UserBookSortType
import java.util.*

@Service
@Validated
class UserBookService(
    private val userBookDomainService: UserBookDomainService
) {
    fun upsertUserBook(@Valid upsertUserBookRequest: UpsertUserBookRequest): UserBookResponse {
        val userBookInfoVO = userBookDomainService.upsertUserBook(
            upsertUserBookRequest.validUserId(),
            upsertUserBookRequest.validBookId(),
            upsertUserBookRequest.validBookIsbn(),
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

    fun findAllByUserIdAndBookIsbnIn(@Valid userBooksByIsbnsRequest: UserBooksByIsbnsRequest): List<UserBookResponse> {
        val userBooks = userBookDomainService.findAllByUserIdAndBookIsbnIn(
            userBooksByIsbnsRequest.validUserId(),
            userBooksByIsbnsRequest.validIsbns(),
        )
        return userBooks.map { UserBookResponse.from(it) }
    }

    fun findUserBookStatusByIsbn(userId: UUID, isbn: String): BookStatus? {
        val userBook = userBookDomainService.findByUserIdAndBookIsbn(userId, isbn)
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
}
