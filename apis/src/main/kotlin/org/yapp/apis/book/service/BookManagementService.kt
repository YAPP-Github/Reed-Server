package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookCreateRequest
import org.yapp.domain.book.Book
import org.yapp.domain.book.BookDomainService
import java.util.UUID

@Service
class BookManagementService(
    private val bookDomainService: BookDomainService
) {
    fun findOrCreateBook(request: BookCreateRequest): Book {
        val isbn = request.validIsbn()

        return bookDomainService.findByIsbn(isbn)
            ?: bookDomainService.save(
                isbn = isbn,
                title = request.validTitle(),
                author = request.validAuthor(),
                publisher = request.validPublisher(),
                coverImageUrl = request.coverImageUrl,
                publicationYear = request.publicationYear,
                description = request.description
            )
    }

}
