package org.yapp.gateway.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.SecurityFilterChain

/**
 * Security configuration for the gateway.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationConverter: Converter<Jwt, out AbstractAuthenticationToken>,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler
) {
    companion object {
        private val WHITELIST_URLS = arrayOf(
            "/api/v1/auth/refresh",
            "/api/v1/auth/signin",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/kakao-login.html/**",
            "/error",
            "/"
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
                it.accessDeniedHandler(customAccessDeniedHandler)
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                }
            }
            .authorizeHttpRequests {
                it.requestMatchers(*WHITELIST_URLS).permitAll()
                it.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                it.requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")
                it.requestMatchers("/api/v1/auth/**").authenticated()
                it.requestMatchers("/api/v1/books/**").authenticated()
                it.anyRequest().authenticated()
            }

        return http.build()
    }
}
