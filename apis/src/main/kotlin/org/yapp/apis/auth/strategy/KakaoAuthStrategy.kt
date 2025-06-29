package org.yapp.apis.auth.strategy

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.AuthCredentials
import org.yapp.apis.auth.dto.KakaoAuthCredentials
import org.yapp.apis.auth.dto.UserCreateInfo
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.helper.KakaoApiHelper
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.auth.ProviderType
import org.yapp.infra.external.oauth.kakao.response.KakaoUserInfo

/**
 * Implementation of AuthStrategy for Kakao authentication.
 */
@Component
class KakaoAuthStrategy(
    private val kakaoApiHelper: KakaoApiHelper
) : AuthStrategy {

    private val log = KotlinLogging.logger {}

    override fun getProviderType(): ProviderType = ProviderType.KAKAO

    override fun authenticate(credentials: AuthCredentials): UserCreateInfo {
        return try {
            val kakaoCredentials = validateCredentials(credentials)
            val kakaoUser = kakaoApiHelper.getUserInfo(kakaoCredentials.accessToken)
            createUserInfo(kakaoUser)
        } catch (exception: Exception) {
            log.error("Kakao authentication failed", exception)
            when (exception) {
                is AuthException -> throw exception
                else -> throw AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, exception.message)
            }
        }
    }

    private fun validateCredentials(credentials: AuthCredentials): KakaoAuthCredentials {
        return credentials as? KakaoAuthCredentials
            ?: throw AuthException(
                AuthErrorCode.INVALID_CREDENTIALS,
                "Credentials must be KakaoAuthCredentials"
            )
    }

    private fun createUserInfo(kakaoUser: KakaoUserInfo): UserCreateInfo {
        return UserCreateInfo.of(
            email = kakaoUser.email ?: ("kakao_${kakaoUser.id}@kakao.com"),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = kakaoUser.profileImageUrl,
            providerType = ProviderType.KAKAO,
            providerId = kakaoUser.id.toString()
        )
    }
}
