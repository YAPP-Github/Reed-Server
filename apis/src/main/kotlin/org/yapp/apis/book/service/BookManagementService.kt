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
        return BookCreateResponse.from(bookVO)
    }

}
