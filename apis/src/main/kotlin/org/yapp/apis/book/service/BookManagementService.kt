package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookCreateRequest
import org.yapp.apis.book.exception.BookErrorCode
import org.yapp.apis.book.exception.BookException
import org.yapp.domain.book.Book
import org.yapp.domain.service.domain.BookDomainService

@Service
class BookManagementService(
    private val bookDomainService: BookDomainService,
    private val bookQueryService: BookQueryService
) {
    fun findOrCreateBookByIsbn(validIsbn: String): Book {
        val detail = bookQueryService.getBookDetail(validIsbn)
        val request = BookCreateRequest.create(detail)

        return bookDomainService.findOrCreateBook(BookCreateRequest.from(request))
    }
}
