package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.yapp.infra.external.oauth.kakao.response.KakaoResponse
import org.yapp.infra.external.oauth.kakao.response.KakaoUnlinkResponse

@Component
class KakaoRestClient(
    builder: RestClient.Builder
) {
    companion object {
        private const val BASE_URL = "https://kapi.kakao.com"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val AUTH_SCHEME_KAKAOAK = "KakaoAK"
        private const val PARAM_TARGET_ID_TYPE = "target_id_type"
        private const val PARAM_TARGET_ID = "target_id"
        private const val VALUE_USER_ID = "user_id"
    }

    private val client = builder
        .baseUrl(BASE_URL)
        .build()

    fun getUserInfo(bearerToken: String): KakaoResponse {
        return client.get()
            .uri("/v2/user/me")
            .header(HEADER_AUTHORIZATION, bearerToken)
            .retrieve()
            .body(KakaoResponse::class.java)
            ?: throw IllegalStateException("Kakao API 응답이 null 입니다.")
    }

    fun unlink(adminKey: String, targetId: String): KakaoUnlinkResponse {
        val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestBody.add(PARAM_TARGET_ID_TYPE, VALUE_USER_ID)
        requestBody.add(PARAM_TARGET_ID, targetId)

        return client.post()
            .uri("/v1/user/unlink")
            .header(HEADER_AUTHORIZATION, "$AUTH_SCHEME_KAKAOAK $adminKey")
            .body(requestBody)
            .retrieve()
            .body(KakaoUnlinkResponse::class.java)
            ?: throw IllegalStateException("Kakao unlink API 응답이 null 입니다.")
    }
}
