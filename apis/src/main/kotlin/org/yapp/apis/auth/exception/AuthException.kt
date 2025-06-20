package org.yapp.apis.auth.exception

import org.yapp.global.exception.CommonException

/**
 * Custom exception for authentication-related errors.
 * This exception is thrown when there are issues with authentication processes.
 */
class AuthException : CommonException {

    /**
     * Constructor with error code.
     *
     * @param errorCode The error code.
     */
    constructor(errorCode: AuthErrorCode) : super(errorCode)

    /**
     * Constructor with error code and custom message.
     *
     * @param errorCode The error code.
     * @param message The custom message.
     */
    constructor(errorCode: AuthErrorCode, message: String) : super(errorCode, message)

    /**
     * Constructor with custom message.
     * This constructor is for backward compatibility.
     *
     * @param message The custom message.
     */
    constructor(message: String) : super(AuthErrorCode.INVALID_CREDENTIALS, message)
}
