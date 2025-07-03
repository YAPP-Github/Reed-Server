package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.yapp.infra.external.oauth.kakao.response.KakaoResponse

@Component
class KakaoRestClient(
    builder: RestClient.Builder
) {
    private val client = builder
        .baseUrl("https://kapi.kakao.com")
        .build()

    fun getUserInfo(bearerToken: String): KakaoResponse {
        return client.get()
            .uri("/v2/user/me")
            .header("Authorization", bearerToken)
            .retrieve()
            .body(KakaoResponse::class.java)
            ?: throw IllegalStateException("Kakao API 응답이 null 입니다.")
    }
}
