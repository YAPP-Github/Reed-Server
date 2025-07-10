package org.yapp.domain.service.domain

import org.yapp.domain.book.Book
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.userbook.UserBook
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class UserBookDomainService(
    private val userBookRepository: UserBookRepository
) {

    fun upsertUserBook(userId: UUID, book: Book, status: BookStatus): UserBook {
        val existing = userBookRepository.findByUserIdAndBookIsbn(userId, book.isbn)
        return if (existing != null) {
            val updated = existing.updateStatus(status)
            userBookRepository.save(updated)
        } else {
            val created = UserBook.create(userId, book, status)
            userBookRepository.save(created)
        }
    }

    fun findAllUserBooks(userId: UUID): List<UserBook> {
        return userBookRepository.findAllByUserId(userId)
    }
}
