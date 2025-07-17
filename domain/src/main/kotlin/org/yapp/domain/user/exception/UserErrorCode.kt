package org.yapp.domain.user.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class UserErrorCode(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "유저 정보를 찾을 수 없습니다.");

    override fun getHttpStatus(): HttpStatus = status
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
