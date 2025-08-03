package org.yapp.apis.book.usecase


import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.UserBooksByIsbnsRequest
import org.yapp.apis.auth.service.UserAuthService

import org.yapp.apis.book.dto.request.*
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.service.BookManagementService
import org.yapp.apis.book.service.BookQueryService
import org.yapp.apis.book.service.UserBookService
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    @Qualifier("aladinBookQueryService")
    private val bookQueryService: BookQueryService,
    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService,
    private val bookManagementService: BookManagementService
) {
    fun searchBooks(
        request: BookSearchRequest,
        userId: UUID
    ): BookSearchResponse {
        userAuthService.validateUserExists(userId)

        val searchResponse = bookQueryService.searchBooks(request)
        val booksWithUserStatus = mergeWithUserBookStatus(searchResponse.books, userId)

        return searchResponse.withUpdatedBooks(booksWithUserStatus)
    }

    fun getBookDetail(
        bookDetailRequest: BookDetailRequest,
        userId: UUID
    ): BookDetailResponse {
        userAuthService.validateUserExists(userId)

        val bookDetailResponse = bookQueryService.getBookDetail(bookDetailRequest)
        val isbn13 = bookDetailResponse.isbn13
            ?: return bookDetailResponse.withUserBookStatus(BookStatus.BEFORE_REGISTRATION)
        
        val userBookStatus = userBookService.findUserBookStatusByIsbn(userId, isbn13) 
            ?: BookStatus.BEFORE_REGISTRATION

        return bookDetailResponse.withUserBookStatus(userBookStatus)
    }

    @Transactional
    fun upsertBookToMyLibrary(
        userId: UUID,
        request: UserBookRegisterRequest
    ): UserBookResponse {
        userAuthService.validateUserExists(userId)

        val bookDetailResponse = bookQueryService.getBookDetail(BookDetailRequest.from(request.validBookIsbn()))
        val bookCreateResponse = bookManagementService.findOrCreateBook(BookCreateRequest.from(bookDetailResponse))
        val upsertUserBookRequest = UpsertUserBookRequest.of(
            userId = userId,
            bookCreateResponse,
            status = request.bookStatus,
        )
        val userBookResponse = userBookService.upsertUserBook(upsertUserBookRequest)

        return userBookResponse
    }

    fun getUserLibraryBooks(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): UserBookPageResponse {
        userAuthService.validateUserExists(userId)

        return userBookService.findUserBooksByDynamicConditionWithStatusCounts(userId, status, sort, title, pageable)
    }

    private fun mergeWithUserBookStatus(
        searchedBooks: List<BookSearchResponse.BookSummary>,
        userId: UUID
    ): List<BookSearchResponse.BookSummary> {
        if (searchedBooks.isEmpty()) {
            return emptyList()
        }

        val isbn13List = searchedBooks.map { it.isbn13 }
        val userBookStatusMap = getUserBookStatusMap(isbn13List, userId)

        return searchedBooks.map { bookSummary ->
            val userStatus = userBookStatusMap[bookSummary.isbn13]
            if (userStatus != null) {
                bookSummary.updateStatus(userStatus)
            } else {
                bookSummary
            }
        }
    }

    private fun getUserBookStatusMap(
        isbn13List: List<String>,
        userId: UUID
    ): Map<String, BookStatus> {
        val userBooksResponse = userBookService.findAllByUserIdAndBookIsbnIn(
            UserBooksByIsbnsRequest.of(userId, isbn13List)
        )

        return userBooksResponse.associate { userBook ->
            userBook.bookIsbn to userBook.status
        }
    }
}
