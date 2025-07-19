package org.yapp.domain.book

import org.yapp.domain.book.exception.BookAlreadyExistsException
import org.yapp.domain.book.exception.BookErrorCode
import org.yapp.domain.book.exception.BookNotFoundException
import org.yapp.domain.book.vo.BookInfoVO
import org.yapp.globalutils.annotation.DomainService

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {
    fun findByIsbn(isbn: String): BookInfoVO {
        val book = bookRepository.findById(isbn) ?: throw BookNotFoundException(BookErrorCode.BOOK_NOT_FOUND)
        return BookInfoVO.newInstance(book)
    }

    fun findOrCreate(
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null
    ): BookInfoVO {
        return findByIsbnOrNull(isbn) ?: run {
            val newBook = Book.create(
                isbn = isbn,
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

    private fun findByIsbnOrNull(isbn: String): BookInfoVO? {
        val book = bookRepository.findById(isbn)
        return book?.let { BookInfoVO.newInstance(it) }
    }


    fun save(
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null
    ): BookInfoVO {
        if (bookRepository.existsById(isbn)) {
            throw BookAlreadyExistsException(BookErrorCode.BOOK_ALREADY_EXISTS)
        }

        val book = Book.create(
            isbn = isbn,
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
}
