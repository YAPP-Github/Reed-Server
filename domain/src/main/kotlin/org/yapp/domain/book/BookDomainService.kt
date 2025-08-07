package org.yapp.domain.book

import org.yapp.domain.book.exception.BookAlreadyExistsException
import org.yapp.domain.book.exception.BookErrorCode
import org.yapp.domain.book.exception.BookNotFoundException
import org.yapp.domain.book.vo.BookInfoVO
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {
    fun findById(id: UUID): BookInfoVO {
        val book = bookRepository.findById(id) ?: throw BookNotFoundException(BookErrorCode.BOOK_NOT_FOUND)
        return BookInfoVO.newInstance(book)
    }

    fun findOrCreate(
        isbn13: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null
    ): BookInfoVO {
        return findByIsbn13OrNull(isbn13) ?: run {
            val newBook = Book.create(
                isbn13 = isbn13,
                title = title,
                author = author,
                publisher = publisher,
                coverImageUrl = coverImageUrl,
                publicationYear = publicationYear,
                description = description
            )
            val savedBook = bookRepository.save(newBook)
            BookInfoVO.newInstance(savedBook)
        }
    }

    fun save(
        isbn13: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null,
    ): BookInfoVO {
        if (bookRepository.existsByIsbn13(isbn13)) {
            throw BookAlreadyExistsException(BookErrorCode.BOOK_ALREADY_EXISTS)
        }

        val book = Book.create(
            isbn13 = isbn13,
            title = title,
            author = author,
            publisher = publisher,
            coverImageUrl = coverImageUrl,
            publicationYear = publicationYear,
            description = description
        )

        val savedBook = bookRepository.save(book)
        return BookInfoVO.newInstance(savedBook)
    }

    private fun findByIsbn13OrNull(isbn13: String): BookInfoVO? {
        val book = bookRepository.findByIsbn13(isbn13)
        return book?.let { BookInfoVO.newInstance(it) }
    }
}
