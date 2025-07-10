package org.yapp.apis.book.service

import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse


@Service
class BookQueryService(
    private val aladinBookQueryService: AladinBookQueryService
) {
    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        return aladinBookQueryService.searchBooks(request)
    }

    fun getBookDetail(validIsbn: String): BookDetailResponse {
        val request = BookDetailRequest.of(validIsbn)
        return aladinBookQueryService.lookupBook(request)
    }
}
