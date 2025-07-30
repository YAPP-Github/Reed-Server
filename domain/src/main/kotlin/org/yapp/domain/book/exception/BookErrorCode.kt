package org.yapp.domain.book.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class BookErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK_404_01", "도서 정보를 찾을 수 없습니다."),
    BOOK_ALREADY_EXISTS(HttpStatus.CONFLICT, "BOOK_409_01", "이미 존재하는 도서입니다.");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
