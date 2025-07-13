package org.yapp.gateway.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.yapp.gateway.jwt.JwtTokenProvider
import org.yapp.gateway.jwt.exception.JwtException
import org.yapp.globalutils.exception.ErrorResponse

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractJwtFromRequest(request)

        if (token.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: JwtException) {
            logger.warn("JWT Authentication Failed: ${e.message}")
            handleJwtException(response, e)
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun extractJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return bearerToken?.takeIf { StringUtils.hasText(it) && it.startsWith(BEARER_PREFIX) }
            ?.substring(BEARER_PREFIX.length)
    }

    private fun handleJwtException(response: HttpServletResponse, e: JwtException) {
        val errorCode = e.errorCode

        val errorResponse = ErrorResponse(
            status = errorCode.getHttpStatus().value(),
            code = errorCode.getCode(),
            message = errorCode.getMessage()
        )

        response.status = errorCode.getHttpStatus().value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
