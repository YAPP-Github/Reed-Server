package org.yapp.infra.external.oauth.apple

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.yapp.infra.external.oauth.apple.response.AppleTokenResponse

@Component
class AppleRestClient(
    builder: RestClient.Builder
) {
    companion object {
        private const val BASE_URL = "https://appleid.apple.com/auth"
        private const val CONTENT_TYPE = "application/x-www-form-urlencoded"
    }

    private val client = builder
        .baseUrl(BASE_URL)
        .build()

    fun getTokens(requestBody: MultiValueMap<String, String>): AppleTokenResponse {
        return client.post()
            .uri("/token")
            .contentType(MediaType.valueOf(CONTENT_TYPE))
            .body(requestBody)
            .retrieve()
            .body(AppleTokenResponse::class.java)
            ?: throw IllegalStateException("Apple token response body is null")
    }

    fun revoke(requestBody: MultiValueMap<String, String>) {
        client.post()
            .uri("/revoke")
            .contentType(MediaType.valueOf(CONTENT_TYPE))
            .body(requestBody)
            .retrieve()
            .toBodilessEntity()
    }
}
