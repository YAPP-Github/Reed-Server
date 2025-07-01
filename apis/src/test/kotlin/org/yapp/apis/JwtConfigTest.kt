package org.yapp.apis

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class JwtConfigTest {

    private val logger = LoggerFactory.getLogger(JwtConfigTest::class.java)

    @Value("\${jwt.secret-key:NOT_FOUND}")
    private lateinit var secretKey: String

    @Value("\${jwt.access-token-expiration:0}")
    private var accessTokenExpiration: Long = 0

    @Test
    fun printJwtConfig() {
        logger.info("=== JWT Config in APIs Module ===")
        logger.info("JWT Secret Key: $secretKey")
        logger.info("Access Token Expiration: $accessTokenExpiration")
        logger.info("================================")
        
        // 환경변수도 확인
        logger.info("Environment Variables:")
        logger.info("JWT_SECRET_KEY: ${System.getenv("JWT_SECRET_KEY")}")
        logger.info("JWT_ACCESS_TOKEN_EXPIRATION: ${System.getenv("JWT_ACCESS_TOKEN_EXPIRATION")}")
        
        // 추가로 stdout에도 출력
        System.out.println("=== JWT Config in APIs Module ===")
        System.out.println("JWT Secret Key: $secretKey")
        System.out.println("Access Token Expiration: $accessTokenExpiration")
        System.out.println("================================")
    }
}
