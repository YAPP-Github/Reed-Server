import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    // Web & Filter
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)

    // Metrics & Monitoring
    implementation(Dependencies.Spring.BOOT_STARTER_ACTUATOR)
    implementation(Dependencies.Prometheus.MICROMETER_PROMETHEUS_REGISTRY)

    // Logging
    implementation(Dependencies.Logging.KOTLIN_LOGGING)

    // Test
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
