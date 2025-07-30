package org.yapp.apis.auth.util

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import java.io.StringReader
import java.security.KeyFactory
import java.security.KeyPair
import java.security.Security
import java.security.interfaces.ECPrivateKey

/**
 * Apple JWT 인증에 사용되는 .p8 키 파일 파서
 * 
 * Apple Developer Console에서 다운로드한 .p8 키 파일(PKCS#8 형식)을
 * JWT 서명 및 검증용 Java KeyPair 객체로 변환합니다.
 * 
 * 지원하는 PEM 형식:
 * - PKCS#8 (-----BEGIN PRIVATE KEY-----)
 * - SEC1 (-----BEGIN EC PRIVATE KEY-----)  
 * - OpenSSL KeyPair 형식
 * 
 * @since 1.0
 */
object AppleKeyParser {
    
    /**
     * Apple .p8 키 파일을 파싱하여 KeyPair를 생성합니다.
     * 
     * @param pemString PEM 형식의 키 문자열
     * @return EC KeyPair (P-256 기반)
     * @throws IllegalArgumentException 키 파싱 실패 시
     */
    fun parseKeyPair(pemString: String): KeyPair {
        ensureBouncyCastleProvider()

        return try {
            parsePemKeyPair(pemString.trim())
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse Apple .p8 key file: ${e.message}", e)
        }
    }

    /**
     * BouncyCastle 보안 제공자가 등록되어 있는지 확인하고 필요시 등록합니다.
     */
    private fun ensureBouncyCastleProvider() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    /**
     * PEM 문자열을 파싱하여 형식에 맞는 KeyPair를 반환합니다.
     * 
     * @param pemString 공백이 제거된 PEM 문자열
     * @return 파싱된 KeyPair
     */
    private fun parsePemKeyPair(pemString: String): KeyPair {
        PEMParser(StringReader(pemString)).use { pemParser ->
            val pemObject = pemParser.readObject()
                ?: throw IllegalArgumentException("No PEM objects found in the provided string")

            val converter = JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)

            return when (pemObject) {
                is PEMKeyPair -> {
                    // OpenSSL 형식 - 개인키와 공개키가 함께 포함된 경우
                    converter.getKeyPair(pemObject)
                }
                
                is PrivateKeyInfo -> {
                    // PKCS#8 형식 - Apple .p8 파일의 표준 형식
                    val privateKey = converter.getPrivateKey(pemObject) as ECPrivateKey
                    deriveKeyPairFromPrivateKey(privateKey)
                }
                
                is KeyPair -> {
                    // 이미 KeyPair 객체인 경우
                    pemObject
                }

                else -> {
                    throw IllegalArgumentException(
                        "Unsupported PEM object type: ${pemObject::class.qualifiedName}. " +
                                "Expected PKCS#8 private key format."
                    )
                }
            }
        }
    }

    /**
     * EC 개인키로부터 BouncyCastle 표준 방식을 사용하여 안전하게 KeyPair를 유도합니다.
     * 
     * @param privateKey EC 개인키
     * @return 공개키가 유도된 완전한 KeyPair
     */
    private fun deriveKeyPairFromPrivateKey(privateKey: ECPrivateKey): KeyPair {
        val keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME)
        val publicKey = generatePublicKeyFromPrivateKey(privateKey, keyFactory)
        
        return KeyPair(publicKey, privateKey)
    }

    /**
     * BouncyCastle EC 수학을 사용하여 개인키로부터 공개키를 생성합니다.
     * 
     * @param privateKey EC 개인키
     * @param keyFactory BouncyCastle EC 키 팩토리
     * @return 유도된 공개키
     */
    private fun generatePublicKeyFromPrivateKey(
        privateKey: ECPrivateKey,
        keyFactory: KeyFactory
    ): java.security.PublicKey {
        
        val bcPrivateKey = privateKey as org.bouncycastle.jce.interfaces.ECPrivateKey
        val bcEcParams = bcPrivateKey.parameters
        
        val publicPoint = bcEcParams.g.multiply(bcPrivateKey.d)
        publicPoint.normalize()

        require(!publicPoint.isInfinity) {
            "Invalid private key: derived public key point is at infinity"
        }
        
        val bcPublicKeySpec = org.bouncycastle.jce.spec.ECPublicKeySpec(publicPoint, bcEcParams)
        return keyFactory.generatePublic(bcPublicKeySpec)
    }
}
