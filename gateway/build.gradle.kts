import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.OBSERVABILITY))

    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_SECURITY)
    implementation(Dependencies.Spring.BOOT_STARTER_OAUTH2_RESOURCE_SERVER)

    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
