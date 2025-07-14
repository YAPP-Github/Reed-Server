package org.yapp.gateway.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.yapp.globalutils.exception.CommonErrorCode

@Component
class CustomAuthenticationEntryPoint(
    private val securityErrorResponseWriter: SecurityErrorResponseWriter
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.warn("Not Authenticated Request - uri: ${request.requestURI}", authException)
        securityErrorResponseWriter.write(response, CommonErrorCode.UNAUTHORIZED)
    }
}
