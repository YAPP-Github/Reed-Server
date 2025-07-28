package org.yapp.apis.auth.util

import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import java.io.StringReader
import java.security.KeyPair

object AppleKeyParser {
    fun parseKeyPair(pemString: String): KeyPair {
        val pemReader = PEMParser(StringReader(pemString))
        val pemObject = pemReader.readObject()

        val converter = JcaPEMKeyConverter()
        return converter.getKeyPair(pemObject as PEMKeyPair)
    }
}
