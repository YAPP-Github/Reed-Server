package org.yapp.gateway.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.yapp.globalutils.exception.CommonErrorCode
import org.yapp.globalutils.exception.ErrorResponse
import java.io.IOException

class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.error("Not Authenticated Request", authException)
        log.error("Request Uri : {}", request.requestURI)

        handleAuthenticationException(response)
    }

    private fun handleAuthenticationException(response: HttpServletResponse) {
        val errorCode = CommonErrorCode.UNAUTHORIZED

        val errorResponse = ErrorResponse(
            status = errorCode.getHttpStatus().value(),
            code = errorCode.getCode(),
            message = errorCode.getMessage()
        )

        val responseBody = try {
            objectMapper.writeValueAsString(errorResponse)
        } catch (e: IOException) {
            log.error("Failed to serialize authentication error response", e)
            return
        }

        try {
            response.status = errorCode.getHttpStatus().value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            response.writer.write(responseBody)
        } catch (e: IOException) {
            log.error("Failed to write authentication error response", e)
        }
    }

}
