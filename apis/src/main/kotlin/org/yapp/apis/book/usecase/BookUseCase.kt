package org.yapp.apis.book.usecase


import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.book.dto.request.*
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.BookSearchResponse.BookSummary
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.apis.book.service.BookManagementService
import org.yapp.apis.book.service.BookQueryService
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.service.ReadingRecordService
import org.yapp.apis.user.service.UserService
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class BookUseCase(
    @Qualifier("aladinBookQueryService")
    private val bookQueryService: BookQueryService,
    private val userService: UserService,
    private val userBookService: UserBookService,
    private val bookManagementService: BookManagementService,
    private val readingRecordService: ReadingRecordService
) {
    fun searchBooks(
        request: BookSearchRequest
    ): BookSearchResponse {
        val searchResponse = bookQueryService.searchBooks(request)
        val booksWithUserStatus = mergeWithUserBookStatus(searchResponse.books, null)

        return searchResponse.withUpdatedBooks(booksWithUserStatus)
    }

    fun getBookDetail(
        bookDetailRequest: BookDetailRequest,
        userId: UUID
    ): BookDetailResponse {
        userService.validateUserExists(userId)

        val bookDetailResponse = bookQueryService.getBookDetail(bookDetailRequest)
        val isbn13 = bookDetailResponse.isbn13
            ?: return bookDetailResponse.withUserBookStatus(BookStatus.BEFORE_REGISTRATION)

        val userBookStatus = userBookService.findUserBookStatusByIsbn13(userId, isbn13)
            ?: BookStatus.BEFORE_REGISTRATION

        return bookDetailResponse.withUserBookStatus(userBookStatus)
    }

    @Transactional
    fun upsertBookToMyLibrary(
        userId: UUID,
        request: UserBookRegisterRequest
    ): UserBookResponse {
        userService.validateUserExists(userId)

        val bookDetailResponse = bookQueryService.getBookDetail(BookDetailRequest.from(request.validIsbn13()))
        val bookCreateResponse = bookManagementService.findOrCreateBook(BookCreateRequest.from(bookDetailResponse))
        val upsertUserBookRequest = UpsertUserBookRequest.of(
            userId = userId,
            bookCreateResponse,
            status = request.validBookStatus(),
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
        userService.validateUserExists(userId)

        return userBookService.findUserBooksByDynamicConditionWithStatusCounts(userId, status, sort, title, pageable)
    }

    @Transactional
    fun deleteBookFromMyLibrary(
        userId: UUID,
        userBookId: UUID
    ) {
        userService.validateUserExists(userId)
        userBookService.deleteUserBook(userBookId, userId)
        readingRecordService.deleteAllByUserBookId(userBookId)
    }

    private fun mergeWithUserBookStatus(
        searchedBooks: List<BookSummary>,
        userId: UUID?
    ): List<BookSummary> {
        if (userId == null || searchedBooks.isEmpty()) {
            return searchedBooks
        }

        val isbn13s = searchedBooks.map { it.isbn13 }
        val userBookStatusMap = getUserBookStatusMap(isbn13s, userId)

        return searchedBooks.map { bookSummary ->
            userBookStatusMap[bookSummary.isbn13]
                ?.let { bookSummary.updateStatus(it) }
                ?: bookSummary
        }
    }

    private fun getUserBookStatusMap(
        isbn13s: List<String>,
        userId: UUID?
    ): Map<String, BookStatus> {
        if (userId == null) return emptyMap()
        val userBooksResponse = userBookService.findAllByUserIdAndBookIsbn13In(
            UserBooksByIsbn13sRequest.of(userId, isbn13s)
        )

        return userBooksResponse.associate { userBook ->
            userBook.isbn13 to userBook.status
        }
    }
}
