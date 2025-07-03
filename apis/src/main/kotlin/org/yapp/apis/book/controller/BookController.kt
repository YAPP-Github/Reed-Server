package org.yapp.apis.book.controller

import BookSearchResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.usecase.BookUseCase


@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookUseCase: BookUseCase
) : BookControllerApi {

    @GetMapping("/search")
    override fun searchBooks(@Valid request: BookSearchRequest): ResponseEntity<BookSearchResponse> {
        val response = bookUseCase.searchBooks(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{itemId}")
    override fun getBookDetail(
        @PathVariable itemId: String,
        @RequestParam(defaultValue = "ISBN") itemIdType: String,
        @RequestParam(required = false) optResult: List<String>?
    ): ResponseEntity<BookDetailResponse> {
        val response = bookUseCase.getBookDetail(itemId, itemIdType, optResult)
        return ResponseEntity.ok(response)
    }
}
