package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.domain.book.Book
import org.yapp.domain.book.BookStatus
import org.yapp.domain.book.service.UserBookDomainService
import java.util.*

@Service
class UserBookService(
    private val userBookDomainService: UserBookDomainService
) {

    fun upsertUserBook(userId: UUID, book: Book, status: BookStatus) =
        userBookDomainService.upsertUserBook(userId, book, BookStatus.valueOf(status.name))

    fun findAllUserBooks(userId: UUID) = userBookDomainService.findAllUserBooks(userId)
}
