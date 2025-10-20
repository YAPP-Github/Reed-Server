package org.yapp.gateway.filter

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.yapp.observability.logging.filter.BaseMdcLoggingFilter

/**
 * Spring Security와 JWT 인증이 있는 환경에서 사용하는 MDC 로깅 필터
 *
 * SecurityContext에서 JWT 토큰을 읽어 사용자 ID를 MDC에 추가합니다.
 * API 서버(apis), 관리자 서버(admin) 등 인증이 필요한 서비스에 사용됩니다.
 */
@Component
class SecurityMdcLoggingFilter : BaseMdcLoggingFilter() {
    /**
     * SecurityContext에서 JWT principal을 읽어 사용자 ID를 추출합니다.
     *
     * @return JWT subject (사용자 ID) 또는 null
     */
    override fun resolveUserId(): String? {
        val authentication = SecurityContextHolder.getContext().authentication ?: return null

        return when (val principal = authentication.principal) {
            is Jwt -> principal.subject
            else -> principal?.toString()
        }
    }
}

