package org.yapp.infra.external.oauth.google

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.util.LinkedMultiValueMap
import org.yapp.infra.external.oauth.google.response.GoogleTokenResponse
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

    fun exchangeAuthorizationCode(
        code: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        url: String,
    ): GoogleTokenResponse {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("code", code)
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("redirect_uri", redirectUri)
            add("grant_type", "authorization_code")
        }

        return client.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(GoogleTokenResponse::class.java)
            ?: throw IllegalStateException("Google Token API 응답이 null 입니다.")
    }

    fun exchangeIdToken(
        idToken: String,
        clientId: String,
        clientSecret: String,
        url: String,
    ): GoogleTokenResponse {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
            add("assertion", idToken)
            add("client_id", clientId)
            add("client_secret", clientSecret)
        }

        return client.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(GoogleTokenResponse::class.java)
            ?: throw IllegalStateException("Google Token API 응답이 null 입니다.")
    }

    fun revoke(requestBody: LinkedMultiValueMap<String, String>) {
        client.post()
            .uri("https://oauth2.googleapis.com/revoke")
            .body(requestBody)
            .retrieve()
            .body(Unit::class.java) // Google revoke API typically returns an empty body
    }
}
