package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.UpsertUserBookRequest
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.domain.book.Book
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.BookStatus
import java.util.*

@Service
class UserBookService(
    private val userBookDomainService: UserBookDomainService
) {
    fun upsertUserBook(upsertUserBookRequest: UpsertUserBookRequest): UserBookResponse =
        UserBookResponse.from(
            userBookDomainService.upsertUserBook(
                upsertUserBookRequest.userId,
                upsertUserBookRequest.bookIsbn,
                upsertUserBookRequest.bookPublisher,
                upsertUserBookRequest.bookAuthor,
                upsertUserBookRequest.bookTitle,
                upsertUserBookRequest.bookCoverImageUrl,
            )
        )

    fun findAllUserBooks(userId: UUID): List<UserBookResponse> =
        userBookDomainService.findAllUserBooks(userId).map { UserBookResponse.from(it) }
}
