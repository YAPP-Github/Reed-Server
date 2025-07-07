package org.yapp.apis.book.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.BookException
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.domain.book.Book
import org.yapp.domain.book.exception.BookErrorCode
import org.yapp.domain.book.service.BookDomainService

@Service
class BookService(
    private val bookDomainService: BookDomainService,
    private val aladinBookQueryService: AladinBookQueryService
) {
    private val log = KotlinLogging.logger {}

    fun findOrCreateBookByIsbn(isbn: String): Book {
        return bookDomainService.findOrCreateBook(isbn) { validIsbn ->
            val bookDetail = fetchBookDetail(validIsbn)
            val finalIsbn = bookDetail.isbn ?: bookDetail.isbn13
            ?: throw BookException(BookErrorCode.ALADIN_API_ERROR, "알라딘 API 응답에 ISBN 없음: $validIsbn")

            Book.create(
                isbn = finalIsbn,
                title = bookDetail.title,
                author = bookDetail.author,
                publisher = bookDetail.publisher,
                publicationYear = bookDetail.pubDate
                    ?.takeIf { it.length >= 4 && it.substring(0, 4).all { ch -> ch.isDigit() } }
                    ?.substring(0, 4)
                    ?.toIntOrNull(),
                coverImageUrl = bookDetail.cover,
                description = bookDetail.description
            )
        }
    }

    private fun fetchBookDetail(isbn: String): BookDetailResponse {
        return try {
            aladinBookQueryService.lookupBook(BookDetailRequest.of(isbn))
        } catch (e: Exception) {
            log.error("알라딘 API 호출 실패. ISBN: {}, error: {}", isbn, e.message)
            throw BookException(BookErrorCode.ALADIN_API_ERROR, "알라딘 API 에러: ${isbn}")
        }
    }
}
