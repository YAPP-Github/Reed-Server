object Plugins {
    const val SPRING_BOOT = "org.springframework.boot"
    const val SPRING_DEPENDENCY_MANAGEMENT = "io.spring.dependency-management"
    const val DETEKT = "io.gitlab.arturbosch.detekt"
    const val KOVER = "org.jetbrains.kotlinx.kover"
    const val JACOCO = "jacoco"
    const val SONAR_QUBE = "org.sonarqube"

    object Kotlin {
        const val ALLOPEN = "org.jetbrains.kotlin.plugin.allopen"
        const val SPRING = "org.jetbrains.kotlin.plugin.spring"
        const val JPA = "org.jetbrains.kotlin.plugin.jpa"
        const val JVM = "org.jetbrains.kotlin.jvm"
        const val KAPT = "org.jetbrains.kotlin.kapt"

        object Short {
            const val KAPT = "kapt"
            const val JVM = "jvm"
            const val SPRING = "plugin.spring"
            const val JPA = "plugin.jpa"
        }
    }
}
