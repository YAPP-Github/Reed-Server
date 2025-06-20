package org.yapp.gateway.jwt.exception

import org.springframework.http.HttpStatus
import org.yapp.global.exception.BaseErrorCode

/**
 * Error codes for JWT-related errors.
 */
enum class JwtErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT_001", "Invalid JWT signature"),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_002", "Invalid JWT token"),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_003", "Expired JWT token"),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT_004", "Unsupported JWT token"),
    EMPTY_JWT_CLAIMS(HttpStatus.UNAUTHORIZED, "JWT_005", "JWT claims string is empty"),
    INVALID_USER_ID(HttpStatus.UNAUTHORIZED, "JWT_006", ""),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "JWT_007", "JWT 토큰 타입이 잘못되었거나 누락되었습니다.");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}