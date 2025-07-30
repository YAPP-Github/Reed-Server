package org.yapp.apis.auth.manager

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.infra.external.oauth.kakao.KakaoApi
import org.yapp.infra.external.oauth.kakao.response.KakaoUserInfo

@Component
class KakaoApiManager(
    private val kakaoApi: KakaoApi
) {
    private val log = KotlinLogging.logger {}

    fun getUserInfo(accessToken: String): KakaoUserInfo {
        return kakaoApi.fetchUserInfo(accessToken)
            .onSuccess { userInfo ->
                log.info("Successfully fetched Kakao user info for userId: ${userInfo.id}")
            }
            .getOrElse { exception ->
                log.error("Failed to fetch Kakao user info", exception)

                when (exception) {
                    is HttpClientErrorException -> {
                        throw AuthException(
                            AuthErrorCode.INVALID_OAUTH_TOKEN,
                            "Invalid Kakao OAuth token."
                        )
                    }

                    is HttpServerErrorException, is Exception -> {
                        throw AuthException(
                            AuthErrorCode.OAUTH_SERVER_ERROR,
                            "Failed to communicate with Kakao server."
                        )
                    }

                    else -> throw exception
                }
            }
    }
}
