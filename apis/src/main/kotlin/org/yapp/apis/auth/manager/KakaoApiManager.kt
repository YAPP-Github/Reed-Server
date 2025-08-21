package org.yapp.apis.auth.manager

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.config.KakaoOauthProperties
import org.yapp.infra.external.oauth.kakao.KakaoApi
import org.yapp.infra.external.oauth.kakao.response.KakaoUnlinkResponse
import org.yapp.infra.external.oauth.kakao.response.KakaoUserInfo

@Component
class KakaoApiManager(
    private val kakaoApi: KakaoApi,
    private val kakaoOauthProperties: KakaoOauthProperties
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
                    is HttpClientErrorException -> throw AuthException(
                        AuthErrorCode.INVALID_OAUTH_TOKEN,
                        "Invalid Kakao Access Token."
                    )

                    else -> throw AuthException(
                        AuthErrorCode.OAUTH_SERVER_ERROR,
                        "Failed to communicate with Kakao server."
                    )
                }
            }
    }

    fun unlink(targetId: String): KakaoUnlinkResponse {
        return kakaoApi.unlink(kakaoOauthProperties.adminKey, targetId)
            .onSuccess { response ->
                log.info("Successfully unlinked Kakao user with targetId: $targetId, responseId: ${response.id}")
                validateUnlinkResponse(response, targetId)
            }
            .getOrElse { exception ->
                log.error("Failed to unlink Kakao user with targetId: $targetId", exception)

                when (exception) {
                    is HttpClientErrorException -> throw AuthException(
                        AuthErrorCode.KAKAO_UNLINK_FAILED,
                        "Failed to unlink Kakao user due to client error: ${exception.message}"
                    )

                    else -> throw AuthException(
                        AuthErrorCode.OAUTH_SERVER_ERROR,
                        "Failed to unlink Kakao user due to server error: ${exception.message}"
                    )
                }
            }
    }

    private fun validateUnlinkResponse(response: KakaoUnlinkResponse, expectedTargetId: String) {
        if (response.id != expectedTargetId) {
            throw AuthException(
                AuthErrorCode.KAKAO_UNLINK_RESPONSE_MISMATCH,
                "Kakao unlink response ID does not match target ID"
            )
        }
    }
}
