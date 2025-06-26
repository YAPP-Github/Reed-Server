package org.yapp.infra.external.oauth.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoResponse internal constructor(
    val id: Long,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount?
)

data class KakaoAccount internal constructor(
    val email: String?,
    val profile: KakaoProfile?
)

data class KakaoProfile internal constructor(
    val nickname: String?,
    @JsonProperty("profile_image_url")
    val profileImageUrl: String?
)
