package org.yapp.apis.auth.manager

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo

@Component
class GoogleApiManager(
    @Value("\${oauth.google.url.user-info}") private val userInfoUrl: String,
    private val restClient: RestClient,
) {
    fun getUserInfo(accessToken: String): GoogleUserInfo {
        return restClient.get()
            .uri(userInfoUrl)
            .headers { it.setBearerAuth(accessToken) }
            .retrieve()
            .body(GoogleUserInfo::class.java)!!
    }
}
