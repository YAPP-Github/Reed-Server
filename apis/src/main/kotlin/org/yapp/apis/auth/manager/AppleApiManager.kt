package org.yapp.apis.auth.manager

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.helper.apple.AppleClientSecretGenerator
import org.yapp.apis.config.AppleOauthProperties
import org.yapp.infra.external.oauth.apple.AppleApi
import org.yapp.infra.external.oauth.apple.response.AppleTokenResponse

@Component
class AppleApiManager(
    private val appleApi: AppleApi,
    private val properties: AppleOauthProperties,
    private val appleClientSecretGenerator: AppleClientSecretGenerator
) {
    private val log = KotlinLogging.logger {}

    fun fetchAppleOauthTokens(authorizationCode: String): AppleTokenResponse {
        val clientSecret = appleClientSecretGenerator.generateClientSecret()

        return appleApi.getOauthTokens(
            clientId = properties.clientId,
            clientSecret = clientSecret,
            code = authorizationCode
        ).onSuccess { response ->
            log.info {
                "Successfully fetched Apple OAuth tokens. user_id=${response.idToken.substring(0, 20)}..."
            }
        }.getOrElse { originalError ->
            log.error(originalError) { "Failed to fetch Apple OAuth tokens." }
            throw AuthException(
                AuthErrorCode.OAUTH_SERVER_ERROR, "Failed to communicate with Apple OAuth server."
            )
        }
    }
}
