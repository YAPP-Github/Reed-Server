package org.yapp.apis.book.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yapp.apis.auth.usecase.AuthUseCase
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.usecase.BookUseCase
import org.yapp.apis.util.AuthUtils


@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookUseCase: BookUseCase,
    private val authUseCase: AuthUseCase
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

    @PostMapping("/upsert")
    override fun upsertBookToMyLibrary(
        @RequestHeader("Authorization") authorization: String,
        @Valid @RequestBody request: UserBookRegisterRequest
    ): ResponseEntity<UserBookResponse> {
        val currentUserId = AuthUtils.extractUserIdFromAuthHeader(authorization, authUseCase::getUserIdFromAccessToken)
        val response = bookUseCase.upsertBookToMyLibrary(currentUserId, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my-library")
    override fun getUserLibraryBooks(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<List<UserBookResponse>> {
        val currentUserId = AuthUtils.extractUserIdFromAuthHeader(authorization, authUseCase::getUserIdFromAccessToken)
        val response = bookUseCase.getUserLibraryBooks(currentUserId)
        return ResponseEntity.ok(response)
    }
}
