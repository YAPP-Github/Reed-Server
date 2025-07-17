package org.yapp.domain.token.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class TokenErrorCode(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_001", "토큰 정보를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_002", "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_003", "리프레시 토큰이 만료되었습니다.");

    override fun getHttpStatus(): HttpStatus = status
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
