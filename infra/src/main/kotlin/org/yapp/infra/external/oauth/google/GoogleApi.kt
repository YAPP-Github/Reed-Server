package org.yapp.infra.external.oauth.google

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.yapp.infra.external.oauth.google.response.GoogleTokenResponse
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo

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
    ): Result<GoogleUserInfo> {
        return runCatching {
            googleRestClient.getUserInfo(BEARER_PREFIX + accessToken, userInfoUrl)
        }
    }

    fun exchangeAuthorizationCode(
        code: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        tokenExchangeUrl: String,
    ): Result<GoogleTokenResponse> {
        return runCatching {
            googleRestClient.exchangeAuthorizationCode(code, clientId, clientSecret, redirectUri, tokenExchangeUrl)
        }
    }

    fun exchangeIdToken(
        idToken: String,
        clientId: String,
        clientSecret: String,
        tokenExchangeUrl: String,
    ): Result<GoogleTokenResponse> {
        return runCatching {
            googleRestClient.exchangeIdToken(idToken, clientId, clientSecret, tokenExchangeUrl)
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
