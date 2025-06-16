import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(Dependencies.Projects.GLOBAL_UTILS))
    implementation(project(Dependencies.Projects.DOMAIN))
    implementation(Dependencies.Spring.BOOT_STARTER_WEB)
    implementation(Dependencies.Spring.BOOT_STARTER_DATA_JPA)
    testImplementation(Dependencies.Spring.BOOT_STARTER_TEST)
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
