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
        val bookInfoVO = bookDomainService.findOrCreate(
            request.validIsbn(),
            request.validTitle(),
            request.validAuthor(),
            request.validPublisher(),
            request.validCoverImageUrl(),
            request.publicationYear,
            request.description
        )
        return BookCreateResponse.from(bookInfoVO)
    }

}
