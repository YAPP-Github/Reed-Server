package org.yapp.apis.book.usecase

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.book.dto.request.BookCreateRequest
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.constant.BookQueryServiceQualifier
import org.yapp.apis.book.service.BookManagementService
import org.yapp.apis.book.service.BookQueryService
import org.yapp.apis.book.service.UserBookService
import org.yapp.globalutils.annotation.UseCase
import java.util.UUID

@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService,
    @Qualifier(BookQueryServiceQualifier.ALADIN)
    private val bookQueryService: BookQueryService,
    private val bookManagementService: BookManagementService
) {
    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        return bookQueryService.searchBooks(request)
    }

    fun getBookDetail(bookDetailRequest: BookDetailRequest): BookDetailResponse {
        return bookQueryService.getBookDetail(bookDetailRequest)
    }

    @Transactional
    fun upsertBookToMyLibrary(userId: UUID, request: UserBookRegisterRequest): UserBookResponse {
        userAuthService.validateUserExists(userId)

        val detail = bookQueryService.getBookDetail(BookDetailRequest.of(request.validBookIsbn()))
        val book = bookManagementService.findOrCreateBook(BookCreateRequest.create(detail))
        val userBook = userBookService.upsertUserBook(userId, book, request.bookStatus)
        return UserBookResponse.from(userBook)
    }

    fun getUserLibraryBooks(userId: UUID): List<UserBookResponse> {
        userAuthService.validateUserExists(userId)

        return userBookService.findAllUserBooks(userId)
            .map(UserBookResponse::from)
    }
}
