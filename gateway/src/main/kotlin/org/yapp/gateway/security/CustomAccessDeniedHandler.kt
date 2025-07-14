package org.yapp.gateway.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.yapp.globalutils.exception.CommonErrorCode

@Component
class CustomAccessDeniedHandler(
    private val securityErrorResponseWriter: SecurityErrorResponseWriter
) : AccessDeniedHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.warn("Access Denied - uri: ${request.requestURI}", accessDeniedException)
        securityErrorResponseWriter.write(response, CommonErrorCode.FORBIDDEN)
    }
}
