package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.domain.book.Book
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.vo.UserBookVO
import java.util.UUID

@Service
class UserBookService(
    private val userBookDomainService: UserBookDomainService
) {
    fun upsertUserBook(userId: UUID, book: Book, status: BookStatus) =
        userBookDomainService.upsertUserBook(userId, book, status)

    fun findAllUserBooks(userId: UUID) =
        userBookDomainService.findAllUserBooks(userId)

    fun findAllByUserIdAndBookIsbnIn(userId: UUID, isbns: List<String>): List<UserBookResponse> {
        return userBookDomainService
            .findAllByUserIdAndBookIsbnIn(userId, isbns).map { UserBookResponse.from(it) }
    }

}

