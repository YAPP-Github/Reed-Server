import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id(Plugins.SPRING_BOOT) version Versions.SPRING_BOOT
    id(Plugins.SPRING_DEPENDENCY_MANAGEMENT) version Versions.SPRING_DEPENDENCY_MANAGEMENT
    kotlin(Plugins.Kotlin.Short.KAPT) version Versions.KOTLIN
    kotlin(Plugins.Kotlin.Short.JVM) version Versions.KOTLIN
    kotlin(Plugins.Kotlin.Short.SPRING) version Versions.KOTLIN
    kotlin(Plugins.Kotlin.Short.JPA) version Versions.KOTLIN
    id(Plugins.DETEKT) version Versions.DETEKT
    id(Plugins.KOVER) version Versions.KOVER
}

allprojects {
    group = "org"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = Plugins.SPRING_BOOT)
    apply(plugin = Plugins.SPRING_DEPENDENCY_MANAGEMENT)
    apply(plugin = Plugins.Kotlin.SPRING)
    apply(plugin = Plugins.Kotlin.JPA)
    apply(plugin = Plugins.Kotlin.JVM)

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(Versions.JAVA_VERSION.toInt()))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    plugins.withId(Plugins.Kotlin.ALLOPEN) {
        extensions.configure<org.jetbrains.kotlin.allopen.gradle.AllOpenExtension> {
            annotation("jakarta.persistence.Entity")
            annotation("jakarta.persistence.MappedSuperclass")
            annotation("jakarta.persistence.Embeddable")
        }
    }

    // Configure Kotlin compiler options
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = Versions.JAVA_VERSION
        }
    }
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
