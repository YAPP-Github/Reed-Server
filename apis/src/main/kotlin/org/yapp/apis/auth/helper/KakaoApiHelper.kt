package org.yapp.apis.auth.helper

import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException

@Component
class KakaoApiHelper(
    private val restTemplate: RestTemplate
) {
    private val log = KotlinLogging.logger {}

    companion object {
        private const val KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me"
    }

    fun fetchUserInfo(accessToken: String): KakaoUserResponse {
        return try {
            val headers = HttpHeaders().apply {
                set("Authorization", "Bearer $accessToken")
            }

            val entity = HttpEntity<String>(headers)
            val response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.GET,
                entity,
                KakaoUserResponse::class.java
            )

            response.body ?: throw AuthException(
                AuthErrorCode.FAILED_TO_GET_USER_INFO,
                "Failed to get user info from Kakao"
            )
        } catch (exception: Exception) {
            log.error("Failed to fetch Kakao user info", exception)
            when (exception) {
                is AuthException -> throw exception
                is RestClientException -> throw AuthException(
                    AuthErrorCode.FAILED_TO_GET_USER_INFO,
                    "Failed to call Kakao API: ${exception.message}"
                )

                else -> throw AuthException(
                    AuthErrorCode.FAILED_TO_GET_USER_INFO,
                    "Unexpected error: ${exception.message}"
                )
            }
        }
    }

    data class KakaoUserResponse(
        val id: Long,
        @JsonProperty("kakao_account")
        val kakaoAccount: KakaoAccount?
    )

    data class KakaoAccount(
        val email: String?,
        val profile: KakaoProfile?
    )

    data class KakaoProfile(
        val nickname: String?,
        @JsonProperty("profile_image_url")
        val profileImageUrl: String?
    )
}
