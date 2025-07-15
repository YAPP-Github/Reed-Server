package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookCreateRequest
import org.yapp.apis.book.dto.response.BookCreateResponse
import org.yapp.domain.book.BookDomainService

@Service
class BookManagementService(
    private val bookDomainService: BookDomainService
) {
    fun findOrCreateBook(request: BookCreateRequest): BookCreateResponse {
        val isbn = request.validIsbn()

        val bookVO = bookDomainService.findByIsbn(isbn)
            ?: bookDomainService.save(
                isbn = isbn,
                title = request.validTitle(),
                author = request.validAuthor(),
                publisher = request.validPublisher(),
                coverImageUrl = request.coverImageUrl,
                publicationYear = request.publicationYear,
                description = request.description
            )
        return BookCreateResponse.from(bookVO)
    }

}
