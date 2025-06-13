import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
