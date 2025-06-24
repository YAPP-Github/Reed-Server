package org.yapp.global.exception

import org.springframework.http.HttpStatus

/**
 * Base interface for all error codes in the system.
 * All error code enums should implement this interface.
 */
interface BaseErrorCode {

    /**
     * Get the HTTP status code associated with this error.
     *
     * @return The HTTP status code.
     */
    fun getHttpStatus(): HttpStatus

    /**
     * Get the unique error code.
     *
     * @return The error code.
     */
    fun getCode(): String

    /**
     * Get the error message.
     *
     * @return The error message.
     */
    fun getMessage(): String
}