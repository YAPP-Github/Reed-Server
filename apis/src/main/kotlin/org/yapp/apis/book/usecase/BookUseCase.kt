package org.yapp.apis.book.usecase

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.UserBooksByIsbnsRequest
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.book.dto.request.BookCreateRequest
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.constant.BookQueryServiceQualifier
import org.yapp.apis.book.dto.request.UpsertUserBookRequest
import org.yapp.apis.book.service.BookManagementService
import org.yapp.apis.book.service.BookQueryService
import org.yapp.apis.book.service.UserBookService
import org.yapp.globalutils.annotation.UseCase
import java.util.UUID

@UseCase
@Transactional(readOnly = true)
class BookUseCase(

    @Qualifier(BookQueryServiceQualifier.ALADIN)
    private val bookQueryService: BookQueryService,

    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService,
    private val bookManagementService: BookManagementService
) {
    fun searchBooks(request: BookSearchRequest, userId: UUID): BookSearchResponse {
        userAuthService.validateUserExists(userId)

        val searchResponse = bookQueryService.searchBooks(request)
        val isbns = searchResponse.books.map { it.isbn }

        val userBooksReponse = userBookService.findAllByUserIdAndBookIsbnIn(UserBooksByIsbnsRequest.of(userId, isbns))
        val statusMap = userBooksReponse.associateBy({ it.bookIsbn }, { it.status })
        searchResponse.books.forEach { bookSummary ->
            statusMap[bookSummary.isbn]?.let { status ->
                bookSummary.updateStatus(status)
            }
        }
        return searchResponse
    }

    fun getBookDetail(bookDetailRequest: BookDetailRequest): BookDetailResponse {
        return bookQueryService.getBookDetail(bookDetailRequest)
    }

    @Transactional
    fun upsertBookToMyLibrary(userId: UUID, request: UserBookRegisterRequest): UserBookResponse {
        userAuthService.validateUserExists(userId)

        val bookDetailResponse = bookQueryService.getBookDetail(BookDetailRequest.of(request.validBookIsbn()))
        val bookCreateResponse = bookManagementService.findOrCreateBook(BookCreateRequest.from(bookDetailResponse))
        val upsertUserBookRequest = UpsertUserBookRequest.of(
            userId = userId,
            bookCreateResponse,
            status = request.bookStatus
        )
        val userBookResponse = userBookService.upsertUserBook(upsertUserBookRequest)

        return userBookResponse
    }

    fun getUserLibraryBooks(userId: UUID): List<UserBookResponse> {
        userAuthService.validateUserExists(userId)

        return userBookService.findAllUserBooks(userId)
    }
}
