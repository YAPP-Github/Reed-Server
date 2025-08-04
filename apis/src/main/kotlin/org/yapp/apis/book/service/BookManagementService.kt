package org.yapp.apis.book.service

import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.yapp.apis.book.dto.request.BookCreateRequest
import org.yapp.apis.book.dto.response.BookCreateResponse
import org.yapp.domain.book.BookDomainService

@Service
@Validated
class BookManagementService(
    private val bookDomainService: BookDomainService
) {
    fun findOrCreateBook(@Valid request: BookCreateRequest): BookCreateResponse {
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
