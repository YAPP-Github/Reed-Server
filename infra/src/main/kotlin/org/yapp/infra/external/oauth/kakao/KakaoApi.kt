package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component

@Component
class KakaoApi internal constructor(
    private val kakaoFeignApi: KakaoFeignApi
) {
    fun fetchUserInfo(accessToken: String): Result<KakaoUserInfo> {
        return runCatching {
            val response = kakaoFeignApi.getUserInfo("Bearer $accessToken")

            KakaoUserInfo(
                id = response.id,
                email = response.kakaoAccount?.email,
                nickname = response.kakaoAccount?.profile?.nickname,
                profileImageUrl = response.kakaoAccount?.profile?.profileImageUrl
            )
        }
    }

    data class KakaoUserInfo(
        val id: Long,
        val email: String?,
        val nickname: String?,
        val profileImageUrl: String?
    )
}
