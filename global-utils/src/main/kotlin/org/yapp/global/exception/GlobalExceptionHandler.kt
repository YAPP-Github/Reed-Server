package org.yapp.global.exception

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


    /**
     * Handle BindException.
     *
     * @param ex The exception.
     * @return The response entity.
     */
    @ExceptionHandler(BindException::class)
    protected fun handleParamViolationException(ex: BindException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.REQUEST_PARAMETER_BIND_FAILED

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message(ex.message ?: "Parameter binding failed")
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

        val error = ErrorResponse.builder()
            .status(errorCode.getHttpStatus().value())
            .message(ex.message ?: errorCode.getMessage())
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

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message("Malformed JSON request: ${ex.localizedMessage}")
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }
}