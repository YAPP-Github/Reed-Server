package org.yapp.apis.auth.helper

import mu.KotlinLogging
import org.springframework.stereotype.Component

import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.infra.external.oauth.kakao.KakaoApi

@Component
class KakaoApiHelper(
    private val kakaoApi: KakaoApi
) {
    private val log = KotlinLogging.logger {}

    fun getUserInfo(accessToken: String): KakaoApi.KakaoUserInfo {
        return kakaoApi.fetchUserInfo(accessToken)
            .onSuccess { userInfo ->
                log.info("Successfully fetched Kakao user info for userId: ${userInfo.id}")
            }
            .getOrElse { exception ->
                log.error("Failed to fetch Kakao user info", exception)
                throw AuthException(
                    AuthErrorCode.FAILED_TO_GET_USER_INFO,
                    "Failed to call Kakao API: ${exception.message}"
                )
            }
    }
}
