import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin(Plugins.Kotlin.Short.KAPT) version Versions.KOTLIN
}

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_REDIS)
    implementation(Dependencies.Spring.KOTLIN_REFLECT)

    implementation(Dependencies.RestClient.HTTP_CLIENT5)
    implementation(Dependencies.RestClient.HTTP_CORE5)

    implementation(Dependencies.Database.MYSQL_CONNECTOR)

    implementation(Dependencies.Flyway.MYSQL)

    implementation(Dependencies.QueryDsl.JPA)

    kapt(Dependencies.QueryDsl.APT)

    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
    testImplementation(Dependencies.TestContainers.MYSQL)
    testImplementation(Dependencies.TestContainers.JUNIT_JUPITER)
    testImplementation(Dependencies.TestContainers.REDIS)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
