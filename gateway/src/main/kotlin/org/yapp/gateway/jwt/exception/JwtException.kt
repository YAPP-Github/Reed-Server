package org.yapp.gateway.jwt.exception

import CommonException


/**
 * Custom exception for JWT-related errors.
 * This exception is thrown when there are issues with JWT token processing.
 */
class JwtException : CommonException {

    /**
     * Constructor with error code.
     *
     * @param errorCode The error code.
     */
    constructor(errorCode: JwtErrorCode) : super(errorCode)

    /**
     * Constructor with error code and custom message.
     *
     * @param errorCode The error code.
     * @param message The custom message.
     */
    constructor(errorCode: JwtErrorCode, message: String) : super(errorCode, message)
}