package org.yapp.infra.external.oauth.kakao.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUnlinkResponse private constructor(
    @JsonProperty("id")
    val id: String
)
