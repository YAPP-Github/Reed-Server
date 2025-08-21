package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component
import org.yapp.infra.external.oauth.kakao.response.KakaoUnlinkResponse
import org.yapp.infra.external.oauth.kakao.response.KakaoUserInfo

@Component
class KakaoApi(
    private val kakaoRestClient: KakaoRestClient
) {
    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    fun fetchUserInfo(accessToken: String): Result<KakaoUserInfo> {
        return runCatching {
            val response = kakaoRestClient.getUserInfo(BEARER_PREFIX + accessToken)
            KakaoUserInfo.from(response)
        }
    }

    fun unlink(adminKey: String, targetId: String): Result<KakaoUnlinkResponse> {
        return runCatching {
            kakaoRestClient.unlink(adminKey, targetId)
        }
    }
}
