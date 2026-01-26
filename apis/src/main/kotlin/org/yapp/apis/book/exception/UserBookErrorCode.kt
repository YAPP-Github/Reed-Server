package org.yapp.apis.book.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class UserBookErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {
    USER_BOOK_ACCESS_DENIED(HttpStatus.FORBIDDEN, "USER_BOOK_403_01", "해당 책에 대한 접근 권한이 없습니다.");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
