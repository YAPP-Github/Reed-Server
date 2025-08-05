package org.yapp.apis.auth.helper.apple

import org.springframework.context.annotation.Profile
import org.springframework.core.io.ResourceLoader
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.util.AppleKeyParser
import org.yapp.apis.config.AppleOauthProperties
import org.yapp.globalutils.annotation.Helper
import java.security.KeyPair

@Helper
@Profile("!test")
class ApplePrivateKeyLoader(
    private val appleProperties: AppleOauthProperties,
    private val resourceLoader: ResourceLoader,
) {
    val keyPair: KeyPair = loadKeyPair()

    private fun loadKeyPair(): KeyPair {
        val resource = resourceLoader.getResource(appleProperties.keyPath)
        if (!resource.exists()) {
            throw AuthException(
                AuthErrorCode.FAILED_TO_LOAD_PRIVATE_KEY,
                "Apple private key file not found at path: ${appleProperties.keyPath}"
            )
        }

        return try {
            val pemString = resource.inputStream.bufferedReader().use { it.readText() }
            AppleKeyParser.parseKeyPair(pemString)
        } catch (e: IllegalArgumentException) {
            throw AuthException(
                AuthErrorCode.INVALID_PRIVATE_KEY_FORMAT,
                "Failed to parse Apple private key: ${e.message}",
            )
        }
    }
}
