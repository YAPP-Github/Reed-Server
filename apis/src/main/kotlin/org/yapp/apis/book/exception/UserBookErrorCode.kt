package org.yapp.apis.book.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class UserBookErrorCode(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {
    USER_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_BOOK_001", "사용자의 책을 찾을 수 없습니다.");

    override fun getHttpStatus(): HttpStatus = status
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
