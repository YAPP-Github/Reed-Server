package org.yapp.infra.external.oauth.apple

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.yapp.infra.external.oauth.apple.response.AppleTokenResponse

@Component
class AppleApi(
    private val appleRestClient: AppleRestClient
) {
    companion object {
        private const val CLIENT_ID = "client_id"
        private const val CLIENT_SECRET = "client_secret"
        private const val CODE = "code"
        private const val GRANT_TYPE = "grant_type"
        private const val TOKEN = "token"
        private const val TOKEN_TYPE_HINT = "token_type_hint"
        private const val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"
    }

    fun getOauthTokens(
        clientId: String,
        clientSecret: String,
        code: String,
        grantType: String = GRANT_TYPE_AUTHORIZATION_CODE
    ): Result<AppleTokenResponse> {
        val requestBody = LinkedMultiValueMap<String, String>().apply {
            add(CLIENT_ID, clientId)
            add(CLIENT_SECRET, clientSecret)
            add(CODE, code)
            add(GRANT_TYPE, grantType)
        }

        return runCatching {
            appleRestClient.getTokens(requestBody)
        }
    }
    
    fun revokeAppleToken(
        clientId: String,
        clientSecret: String,
        token: String,
        tokenTypeHint: String
    ): Result<Unit> {
        val requestBody = LinkedMultiValueMap<String, String>().apply {
            add(CLIENT_ID, clientId)
            add(CLIENT_SECRET, clientSecret)
            add(TOKEN, token)
            add(TOKEN_TYPE_HINT, tokenTypeHint)
        }

        return runCatching {
            appleRestClient.revoke(requestBody)
        }
    }
}
