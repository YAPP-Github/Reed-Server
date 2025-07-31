package org.yapp.apis.book.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.request.UserBooksByIsbnsRequest
import org.yapp.apis.book.dto.request.UpsertUserBookRequest
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.exception.UserBookErrorCode
import org.yapp.apis.book.exception.UserBookNotFoundException
import org.yapp.apis.home.dto.response.UserHomeResponse
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.domain.userbook.vo.UserBookWithLastRecordVO
import java.util.*

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

    fun findRecentReadingBooksForHome(userId: UUID, limit: Int): UserHomeResponse {
        val fetchLimit = maxOf(limit * 2, 10)
        val allUserBookVoInfos = userBookDomainService.findRecentReadingBooksWithLastRecord(userId, fetchLimit)
        val selectedBooks = selectBooksByPriority(allUserBookVoInfos, limit)

        return UserHomeResponse.from(selectedBooks)
    }

    private fun selectBooksByPriority(
        allBooks: List<UserBookWithLastRecordVO>,
        targetLimit: Int
    ): List<UserBookWithLastRecordVO> {
        if (allBooks.isEmpty()) return emptyList()

        val result = mutableListOf<UserBookWithLastRecordVO>()
        val usedBookIds = mutableSetOf<UUID>()

        val readingBooks = allBooks
            .filter { it.status == BookStatus.READING }
            .sortedByDescending { it.lastRecordedAt }

        val readingSelected = readingBooks.take(targetLimit)
        result.addAll(readingSelected)
        usedBookIds.addAll(readingSelected.map { it.id.value })

        if (result.size < targetLimit) {
            val remainingSlots = targetLimit - result.size
            val completedBooks = allBooks
                .filter { it.status == BookStatus.COMPLETED && !usedBookIds.contains(it.id.value) }
                .sortedByDescending { it.lastRecordedAt }

            val completedSelected = completedBooks.take(remainingSlots)
            result.addAll(completedSelected)
            usedBookIds.addAll(completedSelected.map { it.id.value })
        }

        if (result.size < targetLimit) {
            val remainingSlots = targetLimit - result.size
            val remainingBooks = allBooks
                .filter { !usedBookIds.contains(it.id.value) }
                .sortedByDescending { it.lastRecordedAt }

            val remainingSelected = remainingBooks.take(remainingSlots)
            result.addAll(remainingSelected)
        }

        return result
    }

    private fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        pageable: Pageable
    ): Page<UserBookResponse> {
        val page = userBookDomainService.findUserBooksByDynamicCondition(userId, status, sort, pageable)
        return page.map { UserBookResponse.from(it) }
    }

    fun findUserBooksByDynamicConditionWithStatusCounts(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        pageable: Pageable
    ): UserBookPageResponse {
        val userBookResponsePage = findUserBooksByDynamicCondition(userId, status, sort, pageable)
        val userBookStatusCountsVO = userBookDomainService.getUserBookStatusCounts(userId)

        return UserBookPageResponse.of(
            books = userBookResponsePage,
            beforeReadingCount = userBookStatusCountsVO.beforeReadingCount,
            readingCount = userBookStatusCountsVO.readingCount,
            completedCount = userBookStatusCountsVO.completedCount
        )
    }
}
