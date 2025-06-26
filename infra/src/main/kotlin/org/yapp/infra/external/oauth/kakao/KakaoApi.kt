package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component
import org.yapp.infra.external.oauth.kakao.response.KakaoUserInfo

@Component
class KakaoApi internal constructor(
    private val kakaoFeignApi: KakaoFeignApi
) {
    fun fetchUserInfo(accessToken: String): Result<KakaoUserInfo> {
        return runCatching {
            val response = kakaoFeignApi.getUserInfo("Bearer $accessToken")
            KakaoUserInfo.from(response)
        }
    }
}
