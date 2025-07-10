package org.yapp.apis.book.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.service.BookManagementService
import org.yapp.apis.book.service.BookQueryService
import org.yapp.apis.book.service.UserBookService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService,
    private val bookQueryService: BookQueryService,
    private val bookManagementService: BookManagementService
) {
    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        return bookQueryService.searchBooks(request)
    }

    fun getBookDetail(validIsbn: String): BookDetailResponse {
        return bookQueryService.getBookDetail(validIsbn)
    }

    @Transactional
    fun upsertBookToMyLibrary(userId: UUID, request: UserBookRegisterRequest): UserBookResponse {
        userAuthService.findUserById(userId)

        val book = bookManagementService.findOrCreateBookByIsbn(request.validBookIsbn())
        val userBook = userBookService.upsertUserBook(userId, book, request.bookStatus)

        return UserBookResponse.from(userBook)
    }

    fun getUserLibraryBooks(userId: UUID): List<UserBookResponse> {
        userAuthService.findUserById(userId)

        return userBookService.findAllUserBooks(userId)
            .map(UserBookResponse::from)
    }
}
