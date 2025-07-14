package org.yapp.gateway.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.yapp.globalutils.exception.CommonErrorCode
import org.yapp.globalutils.exception.ErrorResponse
import java.io.IOException

class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.warn("Access Denied - uri: ${request.requestURI}", accessDeniedException)
        sendErrorResponse(response)
    }

    private fun sendErrorResponse(response: HttpServletResponse) {
        val errorCode = CommonErrorCode.UNAUTHORIZED

        val errorResponse = ErrorResponse.builder()
            .status(errorCode.getHttpStatus().value())
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build()

        try {
            response.status = errorCode.getHttpStatus().value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        } catch (e: IOException) {
            log.error("Failed to write error response", e)
        }
    }
}
