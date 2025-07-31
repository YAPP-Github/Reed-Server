package org.yapp.apis.book.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.request.UserBooksByIsbnsRequest
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.dto.request.UpsertUserBookRequest
import org.yapp.apis.book.exception.UserBookErrorCode
import org.yapp.apis.book.exception.UserBookNotFoundException
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.UserBookSortType
import java.util.UUID


@Service
class UserBookService(
    private val userBookDomainService: UserBookDomainService
) {
    fun upsertUserBook(upsertUserBookRequest: UpsertUserBookRequest): UserBookResponse {
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

    fun validateUserBookExists(userId: UUID, userBookId: UUID): UserBook {
        return userBookDomainService.findByIdAndUserId(userBookId, userId)
            ?: throw UserBookNotFoundException(
                UserBookErrorCode.USER_BOOK_NOT_FOUND,
                "User book not found with id: $userBookId and userId: $userId"
            )
    }


    fun findAllByUserIdAndBookIsbnIn(userBooksByIsbnsRequest: UserBooksByIsbnsRequest): List<UserBookResponse> {
        val userBooks = userBookDomainService.findAllByUserIdAndBookIsbnIn(
            userBooksByIsbnsRequest.validUserId(),
            userBooksByIsbnsRequest.validIsbns(),
        )
        return userBooks.map { UserBookResponse.from(it) }
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
