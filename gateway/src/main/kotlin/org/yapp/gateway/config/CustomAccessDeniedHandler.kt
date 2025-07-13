package org.yapp.gateway.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
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

    private val log = LoggerFactory.getLogger(CustomAccessDeniedHandler::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.error("No Authorities", accessDeniedException)
        log.error("Request Uri : {}", request.requestURI)

        val errorCode = CommonErrorCode.FORBIDDEN

        val errorResponse = ErrorResponse(
            status = errorCode.getHttpStatus().value(),
            code = errorCode.getCode(),
            message = errorCode.getMessage()
        )

        val responseBody = try {
            objectMapper.writeValueAsString(errorResponse)
        } catch (e: IOException) {
            log.error("Failed to serialize access denied error response", e)
            return
        }

        try {
            response.status = errorCode.getHttpStatus().value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            response.writer.write(responseBody)
        } catch (e: IOException) {
            log.error("Failed to write access denied error response", e)
        }
    }
}
