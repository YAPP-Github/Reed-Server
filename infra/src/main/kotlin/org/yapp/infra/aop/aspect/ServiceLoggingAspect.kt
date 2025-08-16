package org.yapp.infra.aop.aspect

import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.yapp.infra.aop.properties.LoggingAopProperties
import java.time.Duration
import java.time.Instant
import java.util.*

@Aspect
@Component
class ServiceLoggingAspect(
    private val properties: LoggingAopProperties,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(ServiceLoggingAspect::class.java)

    @Around("org.yapp.infra.aop.pointcut.CommonPointcuts.serviceLayer() && !org.yapp.infra.aop.pointcut.CommonPointcuts.noLogging()")
    fun logService(joinPoint: ProceedingJoinPoint): Any? {
        if (!properties.service.enabled) {
            return joinPoint.proceed()
        }

        val signature = joinPoint.signature as MethodSignature
        val startTime = Instant.now()

        logServiceStart(signature, joinPoint.args)

        try {
            val result = joinPoint.proceed()
            logServiceSuccess(signature, startTime, result)
            return result
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun logServiceStart(signature: MethodSignature, args: Array<Any?>) {
        val className = signature.declaringType.simpleName
        val methodName = signature.name
        val params = getArgumentsAsString(signature, args)

        log.info("[SVC-START] {}.{} | Params: {}", className, methodName, truncateIfNeeded(params))
    }

    private fun logServiceSuccess(signature: MethodSignature, startTime: Instant, result: Any?) {
        val className = signature.declaringType.simpleName
        val methodName = signature.name
        val duration = Duration.between(startTime, Instant.now()).toMillis()
        val returnValue = maskSensitiveData(result)

        log.info(
            "[SVC-SUCCESS] {}.{} | Result: {} | Duration: {}ms",
            className,
            methodName,
            truncateIfNeeded(returnValue),
            duration
        )
    }

    private fun getArgumentsAsString(signature: MethodSignature, args: Array<Any?>): String {
        return signature.parameterNames.mapIndexed { index, paramName ->
            "$paramName=${maskSensitiveData(args.getOrNull(index))}"
        }.joinToString(", ")
    }

    private fun maskSensitiveData(obj: Any?): String {
        return when (obj) {
            null -> "null"
            is Unit -> "void"
            is String -> maskStringIfSensitive(obj)
            is Number, is Boolean -> obj.toString()
            is UUID -> obj.toString()
            is Collection<*> -> "[${obj.size} items]"
            else -> maskMapLikeObject(obj)
        }
    }

    private fun maskStringIfSensitive(value: String): String {
        val isSensitive = properties.service.sensitiveFields.any { sensitiveField ->
            value.lowercase().contains(sensitiveField)
        }
        return if (isSensitive) "****" else "\"$value\""
    }

    private fun maskMapLikeObject(obj: Any): String {
        return try {
            val map: Map<*, *> = objectMapper.convertValue(obj, Map::class.java)
            val maskedMap = map.mapValues { (key, value) ->
                val keyStr = key.toString().lowercase()
                val isSensitive = properties.service.sensitiveFields.any { sensitiveField ->
                    keyStr.contains(sensitiveField)
                }
                if (isSensitive) "****" else value
            }
            objectMapper.writeValueAsString(maskedMap)
        } catch (e: Exception) {
            "${obj.javaClass.simpleName}@${Integer.toHexString(obj.hashCode())}"
        }
    }

    private fun truncateIfNeeded(text: String): String {
        return if (text.length > properties.service.maxLogLength) {
            "${text.substring(0, properties.service.maxLogLength)}...[truncated]"
        } else {
            text
        }
    }
}
