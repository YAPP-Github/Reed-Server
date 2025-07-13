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
    fun searchBooks(request: BookSearchRequest, userId: UUID): BookSearchResponse {
        userAuthService.validateUserExists(userId)

        val searchResponse = bookQueryService.searchBooks(request)
        val isbns = searchResponse.books.map { it.isbn }
        val userBooks = userBookService.findAllByUserIdAndBookIsbnIn(userId, isbns)
        val statusMap = userBooks.associateBy({ it.bookIsbn }, { it.status })
        searchResponse.books.forEach { bookSummary ->
            statusMap[bookSummary.isbn]?.let { status ->
                bookSummary.userBookStatus = status
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
