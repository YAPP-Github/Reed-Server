package org.yapp.apis.book.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.service.AladinBookQueryService
import org.yapp.apis.book.service.BookService
import org.yapp.apis.book.service.UserBookService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    private val aladinBookQueryService: AladinBookQueryService,
    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService,
    private val bookService: BookService
) {

    fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        return aladinBookQueryService.searchBooks(request)
    }

    fun getBookDetail(request: BookDetailRequest): BookDetailResponse {
        return aladinBookQueryService.lookupBook(request)
    }

    @Transactional
    fun upsertBookToMyLibrary(userId: UUID, request: UserBookRegisterRequest): UserBookResponse {
        userAuthService.findUserById(userId)

        val book = bookService.findOrCreateBookByIsbn(request.validBookIsbn())
        val userBook = userBookService.upsertUserBook(userId, book, request.bookStatus)

        return UserBookResponse.from(userBook)
    }

    fun getUserLibraryBooks(userId: UUID): List<UserBookResponse> {
        userAuthService.findUserById(userId)

        return userBookService.findAllUserBooks(userId)
            .map { UserBookResponse.from(it) }
    }
}
