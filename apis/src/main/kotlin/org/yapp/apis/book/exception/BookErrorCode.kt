package org.yapp.domain.book.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode


enum class BookErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    /* 404 NOT_FOUND */
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK_001", "Book not found."),

    /* 500 INTERNAL_SERVER_ERROR */
    ALADIN_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BOOK_002", "Error fetching book from external API.");


    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
