package org.yapp.domain.userbook

import org.yapp.domain.book.Book
import org.yapp.domain.userbook.vo.UserBookVO
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

    fun findAllByUserIdAndBookIsbnIn(userId: UUID, isbns: List<String>): List<UserBookVO> {
        if (isbns.isEmpty()) {
            return emptyList()
        }
        return userBookRepository.findAllByUserIdAndBookIsbnIn(userId, isbns)
            .map { UserBookVO.newInstance(it) }
    }
}
