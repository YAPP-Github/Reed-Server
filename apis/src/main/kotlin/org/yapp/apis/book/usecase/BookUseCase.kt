package org.yapp.apis.book.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.book.dto.request.BookDetailRequest // Import BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.service.AladinBookQueryService
import org.yapp.globalutils.annotation.UseCase

@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    private val aladinBookQueryService: AladinBookQueryService
) {
    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        return aladinBookQueryService.searchBooks(request)
    }

    fun getBookDetail(request: BookDetailRequest): BookDetailResponse {
        return aladinBookQueryService.lookupBook(request)
    }
}
