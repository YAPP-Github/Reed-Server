package org.yapp.domain.userbook

import org.yapp.domain.userbook.vo.UserBookInfoVO
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
        val userBook = userBookRepository.findByUserIdAndBookIsbn(userId, bookIsbn)
            ?.apply { updateStatus(status) }
            ?: UserBook.create(
                userId = userId,
                bookIsbn = bookIsbn,
                title = bookTitle,
                author = bookAuthor,
                publisher = bookPublisher,
                coverImageUrl = bookCoverImageUrl,
            )

        val savedUserBook = userBookRepository.save(userBook)
        return UserBookInfoVO.newInstance(savedUserBook)
    }

    fun findAllUserBooks(userId: UUID): List<UserBookInfoVO> {
        return userBookRepository.findAllByUserId(userId)
            .map(UserBookInfoVO::newInstance)
    }
}
