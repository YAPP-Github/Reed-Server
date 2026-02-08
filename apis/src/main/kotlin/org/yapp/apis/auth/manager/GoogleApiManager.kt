package org.yapp.apis.auth.manager

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.config.GoogleOauthProperties
import org.yapp.infra.external.oauth.google.GoogleApi
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo // Changed to GoogleUserInfo

@Component
class GoogleApiManager(
    private val googleApi: GoogleApi,
    private val googleOauthProperties: GoogleOauthProperties,
) {
    private val log = KotlinLogging.logger {}

    fun getUserInfo(accessToken: String): GoogleUserInfo { // Changed to GoogleUserInfo
        return googleApi.fetchUserInfo(accessToken, googleOauthProperties.url.userInfo)
            .onSuccess { userInfo ->
                log.info { "Successfully fetched Google user info for userId: ${userInfo.id}" }
            }
            .getOrElse { exception ->
                log.error(exception) { "Failed to fetch Google user info" }

                when (exception) {
                    is HttpClientErrorException -> throw AuthException(
                        AuthErrorCode.INVALID_OAUTH_TOKEN,
                        "Invalid Google Access Token.",
                    )

                    else -> throw AuthException(
                        AuthErrorCode.OAUTH_SERVER_ERROR,
                        "Failed to communicate with Google server.",
                    )
                }
            }
    }

    fun revokeToken(token: String) {
        googleApi.revokeGoogleToken(token)
            .getOrThrow()
    }
}