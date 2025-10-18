package org.yapp.observability.logging.filter

import org.springframework.stereotype.Component

/**
 * 인증이 필요 없는 환경에서 사용하는 기본 MDC 로깅 필터
 *
 * 이 필터는 사용자 ID를 추출하지 않고 모든 요청을 GUEST로 처리합니다.
 * Batch 애플리케이션이나 인증이 없는 내부 서비스에 사용됩니다.
 */
@Component
class SimpleMdcLoggingFilter : BaseMdcLoggingFilter() {
    /**
     * 인증 정보가 없으므로 null을 반환합니다.
     * MDC에는 GUEST로 기록됩니다.
     */
    override fun resolveUserId(): String? = null
}
