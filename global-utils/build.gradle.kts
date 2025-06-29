import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Uuid.GENERATOR)
    implementation(Dependencies.Logging.KOTLIN_LOGGING)
    implementation(Dependencies.Spring.BOOT_STARTER_VALIDATION)

    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
