package org.yapp.globalutils.global.exception

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Global exception handler for the application.
 * This class handles all exceptions thrown by the application.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {}

    /**
     * Handle BindException.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(BindException::class)
    protected fun handleParamViolationException(ex: BindException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.REQUEST_PARAMETER_BIND_FAILED

        log.warn { "Parameter binding failed: ${ex.message}" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message(ex.message)
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }

    /**
     * Handle CommonException.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(CommonException::class)
    protected fun handleApplicationException(ex: CommonException): ResponseEntity<ErrorResponse> {
        val errorCode = ex.errorCode

        log.warn { "Application exception occurred: ${ex.message}, ErrorCode: ${errorCode.getCode()}" }

        val error = ErrorResponse.builder()
            .status(errorCode.getHttpStatus().value())
            .message(ex.message)
            .code(errorCode.getCode())
            .build()

        return ResponseEntity(error, errorCode.getHttpStatus())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.INVALID_REQUEST

        val fieldErrors = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }

        log.warn { "Validation failed: $fieldErrors" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message(fieldErrors)
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleInvalidJson(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.MALFORMED_JSON

        log.warn { "Malformed JSON request: ${ex.localizedMessage}" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message("Malformed JSON request: ${ex.localizedMessage}")
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }

    @ExceptionHandler(Exception::class)
    protected fun handleGeneralException(ex: Exception): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.INTERNAL_SERVER_ERROR

        log.error(ex) { "Unexpected error occurred: ${ex.message}" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message("An unexpected error occurred")
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }
}
