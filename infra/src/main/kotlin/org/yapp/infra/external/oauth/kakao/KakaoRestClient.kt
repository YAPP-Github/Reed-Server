package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.yapp.infra.external.oauth.kakao.response.KakaoResponse
import org.yapp.infra.external.oauth.kakao.response.KakaoUnlinkResponse

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

    fun unlink(adminKey: String, targetId: String): KakaoUnlinkResponse {
        return client.post()
            .uri("/v1/user/unlink")
            .header("Authorization", "KakaoAK $adminKey")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body("target_id_type=user_id&target_id=$targetId")
            .retrieve()
            .body(KakaoUnlinkResponse::class.java)
            ?: throw IllegalStateException("Kakao unlink API 응답이 null 입니다.")
    }
}
