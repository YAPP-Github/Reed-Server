package org.yapp.infra.external.oauth.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.yapp.infra.external.oauth.kakao.response.KakaoResponse

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
internal interface KakaoFeignApi {

    @GetMapping("/v2/user/me")
    fun getUserInfo(@RequestHeader("Authorization") bearerToken: String): KakaoResponse
}
