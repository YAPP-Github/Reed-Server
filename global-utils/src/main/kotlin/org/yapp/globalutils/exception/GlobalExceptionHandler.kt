package org.yapp.globalutils.exception

import jakarta.validation.ConstraintViolationException
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
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
     * BindException 처리
     *
     * 주로 다음과 같은 경우에 발생합니다:
     * - @ModelAttribute 바인딩 실패
     * - 요청 파라미터 타입 변환 실패
     * - URL 파라미터 바인딩 오류
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
     * 비즈니스 로직 처리 중 발생하는 커스텀 예외 처리
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

    /**
     * 잘못된 인자 전달 (IllegalArgumentException) 처리
     */
    @ExceptionHandler(IllegalArgumentException::class)
    protected fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.INVALID_REQUEST

        log.warn { "Illegal argument: ${ex.message}" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message(ex.message.orEmpty())
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }

    /**
     * @Valid를 통한 요청 본문(@RequestBody) 검증 실패 처리
     */
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

    /**
     * 잘못된 HTTP 메서드로 인한 HttpRequestMethodNotSupportedException를 처리합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.METHOD_NOT_ALLOWED

        log.warn { "HTTP method not supported: ${ex.method} for ${ex.supportedHttpMethods?.joinToString()}" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message("HTTP method '${ex.method}' not supported for this endpoint. Supported methods: ${ex.supportedHttpMethods?.joinToString()}")
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }


    /**
     * 메서드 파라미터 검증 실패(@RequestParam, @PathVariable 등)
     */
    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val commonErrorCode = CommonErrorCode.INVALID_REQUEST

        val constraintErrors = ex.constraintViolations.joinToString(", ") {
            "${it.propertyPath}: ${it.message}"
        }

        log.warn { "Constraint validation failed: $constraintErrors" }

        val error = ErrorResponse.builder()
            .status(commonErrorCode.getHttpStatus().value())
            .message("Constraint validation failed: $constraintErrors")
            .code(commonErrorCode.getCode())
            .build()

        return ResponseEntity(error, commonErrorCode.getHttpStatus())
    }

    /**
     * JSON 파싱 실패 또는 잘못된 형식의 요청 본문 처리
     */
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

    /**
     * 그 외 모든 예외 처리
     *
     * 예외가 위에서 처리되지 않은 경우 이 핸들러가 최종 처리합니다.
     * 사용자에게는 일반적인 오류 메시지를 전달하고, 내부 로그에는 상세 정보를 기록합니다.
     */
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
