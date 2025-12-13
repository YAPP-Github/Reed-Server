package org.yapp.infra.external.oauth.google

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo

@Component
class GoogleRestClient(
    builder: RestClient.Builder
) {
    private val client = builder.build()

    fun getUserInfo(
        bearerToken: String,
        url: String,
    ): GoogleUserInfo {
        return client.get()
            .uri(url)
            .header("Authorization", bearerToken)
            .retrieve()
            .body(GoogleUserInfo::class.java)
            ?: throw IllegalStateException("Google API 응답이 null 입니다.")
    }
}
