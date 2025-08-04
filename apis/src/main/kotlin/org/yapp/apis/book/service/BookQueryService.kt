package org.yapp.apis.book.service

import jakarta.validation.Valid
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse

sealed interface BookQueryService {
    fun searchBooks(request: BookSearchRequest): BookSearchResponse
    fun getBookDetail(@Valid request: BookDetailRequest): BookDetailResponse
}
