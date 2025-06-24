package org.yapp.apis.auth.strategy

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.KakaoAuthCredentials
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User

/**
 * Implementation of AuthStrategy for Kakao authentication.
 */
@Component
class KakaoAuthStrategy(
    private val restTemplate: RestTemplate
) : AuthStrategy {

    companion object {
        private const val KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me"
    }

    override fun getProviderType(): ProviderType = ProviderType.KAKAO

    override fun authenticate(credentials: AuthCredentials): User {
        if (credentials !is KakaoAuthCredentials) {
            throw AuthException(AuthErrorCode.INVALID_CREDENTIALS, "Credentials must be KakaoAuthCredentials")
        }

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${credentials.accessToken}")
            set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
        }

        val entity = HttpEntity<String>(headers)
        val response = restTemplate.exchange(
            KAKAO_USER_INFO_URL,
            HttpMethod.GET,
            entity,
            KakaoUserResponse::class.java
        )

        val kakaoUser = response.body ?: throw AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, "Failed to get user info from Kakao")

        return User(
            email = kakaoUser.kakao_account?.email ?: ("kakao_" + kakaoUser.id),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = kakaoUser.kakao_account?.profile?.profile_image_url,
            providerType = ProviderType.KAKAO,
            providerId = kakaoUser.id.toString()
        )
    }


    data class KakaoUserResponse(
        val id: Long,
        val kakao_account: KakaoAccount?
    )

    data class KakaoAccount(
        val email: String?,
        val profile: KakaoProfile?
    )

    data class KakaoProfile(
        val nickname: String?,
        val profile_image_url: String?
    )
}
