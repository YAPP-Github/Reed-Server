package org.yapp.apis.auth.helper.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.config.GoogleOauthProperties
import org.yapp.globalutils.annotation.Helper
import java.util.Collections

@Helper
class GoogleIdTokenProcessor(
    private val googleOauthProperties: GoogleOauthProperties,
) {
    private val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        ApacheHttpTransport(),
        GsonFactory.getDefaultInstance()
    )
        .setAudience(Collections.singletonList(googleOauthProperties.clientId))
        .build()

    fun parseAndValidate(idToken: String): GoogleIdToken.Payload {
        try {
            val googleIdToken = verifier.verify(idToken)
                ?: throw AuthException(AuthErrorCode.INVALID_GOOGLE_ID_TOKEN, "Invalid ID token")

            return googleIdToken.payload
        } catch (e: Exception) {
            throw AuthException(AuthErrorCode.INVALID_GOOGLE_ID_TOKEN, e.message)
        }
    }
}
