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

    CONFLICT(HttpStatus.CONFLICT, "COMMON_001", "Duplicated Value"),
    REQUEST_PARAMETER_BIND_FAILED(HttpStatus.BAD_REQUEST, "COMMON_002", "PARAMETER_BIND_FAILED"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_003", "BAD REQUEST"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_004", "INVALID REQUEST"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_005", "INTERNAL SERVER ERROR"),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "COMMON_006", "MALFORMED JSON");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}