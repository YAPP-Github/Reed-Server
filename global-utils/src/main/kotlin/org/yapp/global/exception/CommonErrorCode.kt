package org.yapp.global.exception

import org.springframework.http.HttpStatus

/**
 * Common error codes for the application.
 */
enum class CommonErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    CONFLICT(HttpStatus.CONFLICT, "COMMON_001", "리소스 중복"),
    REQUEST_PARAMETER_BIND_FAILED(HttpStatus.BAD_REQUEST, "COMMON_002", "PARAMETER_BIND_FAILED"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_003", "BAD REQUEST");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}