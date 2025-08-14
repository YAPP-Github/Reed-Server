package org.yapp.infra.aop.aspect

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.yapp.infra.aop.properties.LoggingAopProperties
import java.time.Duration
import java.time.Instant

@Aspect
@Component
class ControllerLoggingAspect(
    private val properties: LoggingAopProperties
) {
    private val log = LoggerFactory.getLogger(ControllerLoggingAspect::class.java)

    @Around("org.yapp.infra.aop.pointcut.CommonPointcuts.controller() && !org.yapp.infra.aop.pointcut.CommonPointcuts.noLogging()")
    fun logController(joinPoint: ProceedingJoinPoint): Any? {
        if (!properties.controller.enabled) {
            return joinPoint.proceed()
        }

        val startTime = Instant.now()
        logRequestStart(joinPoint, startTime)

        try {
            val result = joinPoint.proceed()
            logRequestSuccess(startTime)
            return result
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun logRequestStart(joinPoint: ProceedingJoinPoint, startTime: Instant) {
        val signature = joinPoint.signature
        val className = signature.declaringType.simpleName
        val methodName = signature.name

        val request = getCurrentRequest()
        val httpMethod = request?.method ?: "UNKNOWN"
        val uri = request?.requestURI ?: "UNKNOWN"

        log.info(
            "[API-REQ] {} {} | Controller: {}.{} | Start At: {} | TraceId: {} | UserId: {}",
            httpMethod, uri, className, methodName, startTime,
            MDC.get("traceId"), MDC.get("userId")
        )
    }

    private fun logRequestSuccess(startTime: Instant) {
        val endTime = Instant.now()
        val executionTimeMs = getExecutionTimeMs(startTime)
        log.info("[API-RES] End At: {} | Logic Duration: {}ms", endTime, executionTimeMs)
    }

    private fun getCurrentRequest(): HttpServletRequest? =
        (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request

    private fun getExecutionTimeMs(startTime: Instant): Long =
        Duration.between(startTime, Instant.now()).toMillis()
}
