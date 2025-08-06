package org.yapp.infra.external.oauth.kakao

import org.springframework.stereotype.Component
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
        private const val HEADER_CONTENT_TYPE = "Content-Type"

        private const val AUTH_SCHEME_KAKAOAK = "KakaoAK"
        private const val CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded"

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
        val requestBody = "$PARAM_TARGET_ID_TYPE=$VALUE_USER_ID&$PARAM_TARGET_ID=$targetId"

        return client.post()
            .uri("/v1/user/unlink")
            .header(HEADER_AUTHORIZATION, "$AUTH_SCHEME_KAKAOAK $adminKey")
            .header(HEADER_CONTENT_TYPE, CONTENT_TYPE_FORM_URLENCODED)
            .body(requestBody)
            .retrieve()
            .body(KakaoUnlinkResponse::class.java)
            ?: throw IllegalStateException("Kakao unlink API 응답이 null 입니다.")
    }
}
