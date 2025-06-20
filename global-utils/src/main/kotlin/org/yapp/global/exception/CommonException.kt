package org.yapp.global.exception

import org.springframework.web.server.ResponseStatusException

/**
 * Common exception class for all application exceptions.
 * This exception is thrown when there are application-specific errors.
 */
open class CommonException : ResponseStatusException {

    val errorCode: BaseErrorCode

    /**
     * Constructor with error code.
     *
     * @param errorCode The error code.
     */
    constructor(errorCode: BaseErrorCode) : super(errorCode.getHttpStatus(), errorCode.getMessage()) {
        this.errorCode = errorCode
    }

    /**
     * Constructor with error code and custom message.
     *
     * @param errorCode The error code.
     * @param message The custom message.
     */
    constructor(errorCode: BaseErrorCode, message: String) : super(errorCode.getHttpStatus(), message) {
        this.errorCode = errorCode
    }
}
