package org.yapp.gateway.security

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.yapp.observability.metrics.config.ActuatorProperties
import org.yapp.gateway.filter.SecurityMdcLoggingFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(ActuatorProperties::class)
class SecurityConfig(
    private val jwtAuthenticationConverter: Converter<Jwt, out AbstractAuthenticationToken>,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val securityMdcLoggingFilter: SecurityMdcLoggingFilter,
    actuatorProperties: ActuatorProperties
) {
    companion object {
        private val PUBLIC_URLS = arrayOf(
            "/api/v1/books/guest/search",
            "/api/v1/auth/refresh",
            "/api/v1/auth/signin",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/kakao-login.html/**",
            "/favicon.ico",
            "/error",
            "/"
        )
        private const val ADMIN_PATTERN = "/api/v1/admin/**"
    }

    private val whitelistUrls: Array<String> = PUBLIC_URLS + "${actuatorProperties.basePath}/**"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
                it.accessDeniedHandler(customAccessDeniedHandler)
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { it.jwtAuthenticationConverter(jwtAuthenticationConverter) }
            }
            .authorizeHttpRequests {
                it.requestMatchers(*whitelistUrls).permitAll()
                it.requestMatchers(ADMIN_PATTERN).hasRole("ADMIN")
                it.anyRequest().authenticated()
            }
            .addFilterAfter(securityMdcLoggingFilter, BearerTokenAuthenticationFilter::class.java)
            .build()
    }
}
