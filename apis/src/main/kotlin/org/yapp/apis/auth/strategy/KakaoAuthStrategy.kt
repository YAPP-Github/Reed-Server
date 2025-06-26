package org.yapp.apis.auth.strategy

import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.yapp.apis.auth.dto.AuthCredentials
import org.yapp.apis.auth.dto.KakaoAuthCredentials
import org.yapp.apis.auth.dto.UserCreateInfo
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.auth.ProviderType

/**
 * Implementation of AuthStrategy for Kakao authentication.
 */
@Component
class KakaoAuthStrategy(
    private val restTemplate: RestTemplate
) : AuthStrategy {

    private val log = KotlinLogging.logger {}

    companion object {
        private const val KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me"
    }

    override fun getProviderType(): ProviderType = ProviderType.KAKAO

    override fun authenticate(credentials: AuthCredentials): UserCreateInfo {
        return try {
            val kakaoCredentials = credentials as KakaoAuthCredentials
            val kakaoUser = fetchKakaoUserInfo(kakaoCredentials.accessToken)
            createUserInfo(kakaoUser)
        } catch (exception: Exception) {
            log.error("Kakao authentication failed", exception)
            when (exception) {
                is AuthException -> throw exception
                is ClassCastException -> throw AuthException(
                    AuthErrorCode.INVALID_CREDENTIALS,
                    "Credentials must be KakaoAuthCredentials"
                )

                else -> throw AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, exception.message)
            }
        }
    }

    private fun fetchKakaoUserInfo(accessToken: String): KakaoUserResponse {
        try {
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

            return response.body ?: throw AuthException(
                AuthErrorCode.FAILED_TO_GET_USER_INFO,
                "Failed to get user info from Kakao"
            )
        } catch (exception: Exception) {
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

    private fun createUserInfo(kakaoUser: KakaoUserResponse): UserCreateInfo {
        return UserCreateInfo.of(
            email = kakaoUser.kakaoAccount?.email ?: ("kakao_${kakaoUser.id}@kakao.com"),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = kakaoUser.kakaoAccount?.profile?.profileImageUrl,
            providerType = ProviderType.KAKAO,
            providerId = kakaoUser.id.toString()
        )
    }


    private data class KakaoUserResponse(
        val id: Long,
        @JsonProperty("kakao_account")
        val kakaoAccount: KakaoAccount?
    )

    private data class KakaoAccount(
        val email: String?,
        val profile: KakaoProfile?
    )

    private data class KakaoProfile(
        val nickname: String?,
        @JsonProperty("profile_image_url")
        val profileImageUrl: String?
    )
}
