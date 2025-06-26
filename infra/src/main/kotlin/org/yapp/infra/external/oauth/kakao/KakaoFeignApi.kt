package org.yapp.infra.external.oauth.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
internal interface KakaoFeignApi {

    @GetMapping("/v2/user/me")
    fun getUserInfo(@RequestHeader("Authorization") authorization: String): KakaoResponse

    data class KakaoResponse(
        val id: Long,
        @JsonProperty("kakao_account")
        val kakaoAccount: KakaoAccount?
    )

    data class KakaoAccount(
        val email: String?,
        val profile: KakaoProfile?
    )

    data class KakaoProfile(
        val nickname: String?,
        @JsonProperty("profile_image_url")
        val profileImageUrl: String?
    )
}
