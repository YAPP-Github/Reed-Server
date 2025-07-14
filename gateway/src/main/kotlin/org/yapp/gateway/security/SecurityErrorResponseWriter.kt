package org.yapp.gateway.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.yapp.globalutils.exception.CommonErrorCode
import org.yapp.globalutils.exception.ErrorResponse
import java.io.IOException

@Component
class SecurityErrorResponseWriter(
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun write(response: HttpServletResponse, errorCode: CommonErrorCode) {
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
