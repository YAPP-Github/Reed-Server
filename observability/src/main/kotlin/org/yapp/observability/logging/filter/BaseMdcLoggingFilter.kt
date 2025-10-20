package org.yapp.observability.logging.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

/**
 * MDC (Mapped Diagnostic Context) 기반 로깅 필터의 기본 구현
 *
 * 이 필터는 모든 HTTP 요청에 대해 다음 정보를 MDC에 추가합니다:
 * - traceId: 요청 추적 ID (X-Request-ID 헤더에서 가져오거나 자동 생성)
 * - clientIp: 클라이언트 IP (X-Forwarded-For, X-Real-IP 헤더 고려)
 * - requestInfo: HTTP 메서드와 URI
 * - userId: 사용자 ID (서브클래스에서 구현)
 *
 * 서브클래스는 resolveUserId()를 오버라이드하여 사용자 ID 추출 로직을 제공할 수 있습니다.
 */
abstract class BaseMdcLoggingFilter : OncePerRequestFilter() {
    companion object {
        const val TRACE_ID_HEADER = "X-Request-ID"
        const val XFF_HEADER = "X-Forwarded-For"
        const val X_REAL_IP_HEADER = "X-Real-IP"
        const val TRACE_ID_KEY = "traceId"
        const val USER_ID_KEY = "userId"
        const val CLIENT_IP_KEY = "clientIp"
        const val REQUEST_INFO_KEY = "requestInfo"
        const val DEFAULT_GUEST_USER = "GUEST"
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

    /**
     * 요청에서 TraceId를 추출하거나 생성합니다.
     * X-Request-ID 헤더가 있으면 사용하고, 없으면 새로 생성합니다.
     */
    private fun resolveTraceId(request: HttpServletRequest): String {
        val incomingTraceId = request.getHeader(TRACE_ID_HEADER)
        return incomingTraceId?.takeIf { it.isNotBlank() }
            ?: UUID.randomUUID().toString().replace("-", "")
    }

    /**
     * MDC에 로깅 컨텍스트 정보를 추가합니다.
     */
    private fun populateMdc(request: HttpServletRequest, traceId: String) {
        MDC.put(TRACE_ID_KEY, traceId)
        MDC.put(CLIENT_IP_KEY, extractClientIp(request))
        MDC.put(REQUEST_INFO_KEY, "${request.method} ${request.requestURI}")

        val userId = resolveUserId()
        MDC.put(USER_ID_KEY, userId ?: DEFAULT_GUEST_USER)
    }

    /**
     * 클라이언트의 실제 IP 주소를 추출합니다.
     * X-Forwarded-For, X-Real-IP 헤더를 우선 확인하고, 없으면 remoteAddr 사용합니다.
     */
    private fun extractClientIp(request: HttpServletRequest): String {
        val xffHeader = request.getHeader(XFF_HEADER)
        if (!xffHeader.isNullOrBlank()) {
            return xffHeader.split(",").first().trim()
        }

        val xRealIp = request.getHeader(X_REAL_IP_HEADER)
        if (!xRealIp.isNullOrBlank()) {
            return xRealIp.trim()
        }

        return request.remoteAddr
    }

    /**
     * 사용자 ID를 추출합니다.
     * 서브클래스에서 오버라이드하여 Security Context, JWT 등에서 사용자 정보를 추출할 수 있습니다.
     *
     * @return 사용자 ID (null인 경우 GUEST로 처리됨)
     */
    protected abstract fun resolveUserId(): String?
}
