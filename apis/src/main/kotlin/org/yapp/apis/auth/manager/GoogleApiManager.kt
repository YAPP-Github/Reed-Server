package org.yapp.apis.auth.manager

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.config.GoogleOauthProperties
import org.yapp.infra.external.oauth.google.GoogleApi
import org.yapp.infra.external.oauth.google.response.GoogleTokenResponse
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo

@Component
class GoogleApiManager(
    private val googleApi: GoogleApi,
    private val googleOauthProperties: GoogleOauthProperties,
) {
    private val log = KotlinLogging.logger {}

    // Note: ID tokens cannot be exchanged for access tokens with Google's token endpoint.
    // The ID token should be validated directly using GoogleIdTokenProcessor.
    // If an access token is needed, use the authorization code flow instead.

    fun exchangeAuthorizationCode(authorizationCode: String): GoogleTokenResponse {
        val tokenUri = googleOauthProperties.url.tokenUri
            ?: throw AuthException(
                AuthErrorCode.OAUTH_SERVER_ERROR,
                "Google token URI is not configured."
            )

        val redirectUri = googleOauthProperties.redirectUri
            ?: throw AuthException(
                AuthErrorCode.OAUTH_SERVER_ERROR,
                "Google redirect URI is not configured."
            )

        val clientSecret = googleOauthProperties.clientSecret
            ?: throw AuthException(
                AuthErrorCode.OAUTH_SERVER_ERROR,
                "Google client secret is not configured."
            )

        return googleApi.exchangeAuthorizationCode(
            code = authorizationCode,
            clientId = googleOauthProperties.clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri,
            tokenExchangeUrl = tokenUri
        )
            .onSuccess { tokenResponse ->
                log.info { "Successfully exchanged Google authorization code for tokens" }
            }
            .getOrElse { exception ->
                log.error(exception) { "Failed to exchange Google authorization code" }

                when (exception) {
                    is HttpClientErrorException -> throw AuthException(
                        AuthErrorCode.INVALID_OAUTH_TOKEN,
                        "Invalid Google Authorization Code.",
                    )

                    else -> throw AuthException(
                        AuthErrorCode.OAUTH_SERVER_ERROR,
                        "Failed to communicate with Google server.",
                    )
                }
            }
    }

    fun getUserInfo(accessToken: String): GoogleUserInfo {
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
