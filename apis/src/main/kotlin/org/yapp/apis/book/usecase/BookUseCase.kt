package org.yapp.apis.book.usecase

import BookSearchResponse
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.service.BookService
import org.yapp.globalutils.annotation.UseCase


@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    private val bookService: BookService
) {
    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        return bookService.searchBooks(request.query, request.toAladinParams())
    }

    fun getBookDetail(itemId: String, itemIdType: String, optResult: List<String>?): BookDetailResponse {
        return bookService.lookupBook(itemId, itemIdType, optResult)
    }
}
