package org.yapp.gateway.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class MdcLoggingFilter : OncePerRequestFilter() {
    companion object {
        private const val TRACE_ID_HEADER = "X-Request-ID"
        private const val XFF_HEADER = "X-Forwarded-For"
        private const val TRACE_ID_KEY = "traceId"
        private const val USER_ID_KEY = "userId"
        private const val CLIENT_IP_KEY = "clientIp"
        private const val REQUEST_INFO_KEY = "requestInfo"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = resolveTraceId(request)
        populateMdc(request, traceId)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }

    private fun resolveTraceId(request: HttpServletRequest): String {
        val incomingTraceId = request.getHeader(TRACE_ID_HEADER)
        return incomingTraceId?.takeIf { it.isNotBlank() }
            ?: UUID.randomUUID().toString().replace("-", "")
    }

    private fun populateMdc(request: HttpServletRequest, traceId: String) {
        MDC.put(TRACE_ID_KEY, traceId)
        MDC.put(CLIENT_IP_KEY, extractClientIp(request))
        MDC.put(REQUEST_INFO_KEY, "${request.method} ${request.requestURI}")

        val userId = resolveUserId()
        MDC.put(USER_ID_KEY, userId ?: "GUEST")
    }

    private fun extractClientIp(request: HttpServletRequest): String {
        val xffHeader = request.getHeader(XFF_HEADER)
        return if (xffHeader.isNullOrBlank()) {
            request.remoteAddr
        } else {
            xffHeader.split(",").first().trim()
        }
    }

    private fun resolveUserId(): String? {
        val authentication = SecurityContextHolder.getContext().authentication ?: return null

        return when (val principal = authentication.principal) {
            is Jwt -> principal.subject
            else -> principal?.toString()
        }
    }
}

