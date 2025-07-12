package org.yapp.apis.book.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.usecase.BookUseCase
import java.util.UUID

@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookUseCase: BookUseCase,
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

    @PutMapping("/upsert")
    override fun upsertBookToMyLibrary(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody request: UserBookRegisterRequest
    ): ResponseEntity<UserBookResponse> {
        val response = bookUseCase.upsertBookToMyLibrary(userId, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my-library")
    override fun getUserLibraryBooks(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<List<UserBookResponse>> {

        val response = bookUseCase.getUserLibraryBooks(userId)
        return ResponseEntity.ok(response)
    }
}
