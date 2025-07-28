package org.yapp.apis.auth.helper.apple

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.util.AppleKeyParser
import org.yapp.apis.config.AppleOauthProperties
import org.yapp.globalutils.annotation.Helper
import java.security.KeyPair

@Helper
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

        val pemString = resource.inputStream.bufferedReader().use { it.readText() }

        return AppleKeyParser.parseKeyPair(pemString)
    }
}
