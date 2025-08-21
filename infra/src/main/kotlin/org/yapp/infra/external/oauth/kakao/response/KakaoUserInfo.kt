package org.yapp.infra.external.oauth.kakao.response

data class KakaoUserInfo private constructor(
    val id: Long,
    val email: String?,
    val nickname: String?,
    val profileImageUrl: String?
) {
    companion object {
        fun from(kakaoResponse: KakaoResponse): KakaoUserInfo {
            return KakaoUserInfo(
                id = kakaoResponse.id,
                email = kakaoResponse.kakaoAccount?.email,
                nickname = kakaoResponse.kakaoAccount?.profile?.nickname,
                profileImageUrl = kakaoResponse.kakaoAccount?.profile?.profileImageUrl
            )
        }
    }
}
