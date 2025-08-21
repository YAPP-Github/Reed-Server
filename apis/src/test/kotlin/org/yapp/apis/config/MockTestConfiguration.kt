package org.yapp.apis.config

import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.yapp.apis.auth.helper.apple.ApplePrivateKeyLoader
import java.security.KeyPairGenerator

@TestConfiguration
class MockTestConfiguration {

    @Bean
    @Primary
    fun mockApplePrivateKeyLoader(): ApplePrivateKeyLoader {
        val mockLoader = Mockito.mock(ApplePrivateKeyLoader::class.java)

        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(256)
        val keyPair = keyPairGenerator.generateKeyPair()
        Mockito.`when`(mockLoader.keyPair).thenReturn(keyPair)

        return mockLoader
    }
}
