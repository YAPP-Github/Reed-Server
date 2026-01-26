package org.yapp.infra.external.oauth.google

import org.springframework.stereotype.Component
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo

@Component
class GoogleApi(
    private val googleRestClient: GoogleRestClient
) {
    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    fun fetchUserInfo(
        accessToken: String,
        userInfoUrl: String,
    ): Result<GoogleUserInfo> {
        return runCatching {
            googleRestClient.getUserInfo(BEARER_PREFIX + accessToken, userInfoUrl)
        }
    }
}
