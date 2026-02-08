package org.yapp.infra.external.oauth.google

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo // Changed to GoogleUserInfo

@Component
class GoogleApi(
    private val googleRestClient: GoogleRestClient
) {
    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val TOKEN = "token"
    }

    fun fetchUserInfo(
        accessToken: String,
        userInfoUrl: String,
    ): Result<GoogleUserInfo> { // Changed to GoogleUserInfo
        return runCatching {
            googleRestClient.getUserInfo(BEARER_PREFIX + accessToken, userInfoUrl)
        }
    }

    fun revokeGoogleToken(
        token: String
    ): Result<Unit> {
        val requestBody = LinkedMultiValueMap<String, String>().apply {
            add(TOKEN, token)
        }

        return runCatching {
            googleRestClient.revoke(requestBody)
        }
    }
}
