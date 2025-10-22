package org.yapp.batch.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.ByteArrayInputStream
import java.io.IOException

@Configuration
@Profile("!test") // 임시 조치
class FcmConfig {

    @Value("\${FIREBASE_TYPE:service_account}")
    private lateinit var type: String

    @Value("\${FIREBASE_PROJECT_ID:reed-1f3ce}")
    private lateinit var projectId: String

    @Value("\${FIREBASE_PRIVATE_KEY_ID:1d0ad75134b39680e0e0b4b477475cf4266f076d}")
    private lateinit var privateKeyId: String

    @Value("\${FIREBASE_PRIVATE_KEY}")
    private lateinit var privateKey: String

    @Value("\${FIREBASE_CLIENT_EMAIL:firebase-adminsdk-fbsvc@reed-1f3ce.iam.gserviceaccount.com}")
    private lateinit var clientEmail: String

    @Value("\${FIREBASE_CLIENT_ID:113454566071768455640}")
    private lateinit var clientId: String

    @Value("\${FIREBASE_AUTH_URI:https://accounts.google.com/o/oauth2/auth}")
    private lateinit var authUri: String

    @Value("\${FIREBASE_TOKEN_URI:https://oauth2.googleapis.com/token}")
    private lateinit var tokenUri: String

    @Value("\${FIREBASE_AUTH_PROVIDER_X509_CERT_URL:https://www.googleapis.com/oauth2/v1/certs}")
    private lateinit var authProviderX509CertUrl: String

    @Value("\${FIREBASE_CLIENT_X509_CERT_URL:https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40reed-1f3ce.iam.gserviceaccount.com}")
    private lateinit var clientX509CertUrl: String

    @Value("\${FIREBASE_UNIVERSE_DOMAIN:googleapis.com}")
    private lateinit var universeDomain: String

    @Bean
    fun firebaseApp(): FirebaseApp {
        try {
            // Create a JSON string with the Firebase credentials
            val firebaseCredentialsJson = """
                {
                  "type": "$type",
                  "project_id": "$projectId",
                  "private_key_id": "$privateKeyId",
                  "private_key": "$privateKey",
                  "client_email": "$clientEmail",
                  "client_id": "$clientId",
                  "auth_uri": "$authUri",
                  "token_uri": "$tokenUri",
                  "auth_provider_x509_cert_url": "$authProviderX509CertUrl",
                  "client_x509_cert_url": "$clientX509CertUrl",
                  "universe_domain": "$universeDomain"
                }
            """.trimIndent()

            // Create GoogleCredentials from the JSON string
            val googleCredentials = GoogleCredentials.fromStream(
                ByteArrayInputStream(firebaseCredentialsJson.toByteArray())
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
