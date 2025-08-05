package org.yapp.apis.book.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class UserBookErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    USER_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_BOOK_404_01", "사용자의 책을 찾을 수 없습니다.");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
