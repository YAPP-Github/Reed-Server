package org.yapp.apis.book.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.usecase.BookUseCase


@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookUseCase: BookUseCase
) : BookControllerApi {

    @GetMapping("/search")
    override fun searchBooks(@Valid @ModelAttribute request: BookSearchRequest): ResponseEntity<BookSearchResponse> {
        val response = bookUseCase.searchBooks(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/detail")
    override fun getBookDetail(
        @Valid @ModelAttribute request: BookDetailRequest
    ): ResponseEntity<BookDetailResponse> {
        val response = bookUseCase.getBookDetail(request)
        return ResponseEntity.ok(response)
    }
}
