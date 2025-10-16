package org.yapp.batch.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class FcmConfig {

    @Bean
    fun firebaseApp(): FirebaseApp {
        try {
            val googleCredentials = GoogleCredentials.fromStream(
                ClassPathResource("google-services.json").inputStream
            )
            
            val options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build()
            
            return if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            } else {
                FirebaseApp.getInstance()
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to initialize Firebase", e)
        }
    }
}
