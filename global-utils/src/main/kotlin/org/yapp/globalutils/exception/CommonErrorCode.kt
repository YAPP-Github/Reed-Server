package org.yapp.globalutils.exception

import org.springframework.http.HttpStatus

/**
 * Common error codes for the application.
 */
enum class CommonErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    CONFLICT(HttpStatus.CONFLICT, "COMMON_409_01", "Duplicated Value"),
    REQUEST_PARAMETER_BIND_FAILED(HttpStatus.BAD_REQUEST, "COMMON_400_01", "PARAMETER_BIND_FAILED"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400_02", "BAD REQUEST"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400_03", "INVALID REQUEST"),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "COMMON_400_04", "MALFORMED JSON"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401_01", "UNAUTHORIZED"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403_01", "FORBIDDEN"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_404_01", "NOT FOUND"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_405_01", "METHOD NOT ALLOWED"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500_01", "INTERNAL SERVER ERROR"),
    ;


    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
